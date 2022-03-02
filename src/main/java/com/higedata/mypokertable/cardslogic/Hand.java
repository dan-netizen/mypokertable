package com.higedata.mypokertable.cardslogic;

import java.util.ArrayList;
import java.util.Comparator;

public class Hand {
    //private Card[] cards = new Card[7];
    private int[] value;
    private ArrayList<Card> cards2;

    class CardComparator implements Comparator<Card> {
  
        // override the compare() method
        public int compare(Card c1, Card c2) {
            int i = c1.getRank() - c2.getRank();
            if (i == 0)
                return 0;
            else if (i < 0)//using '<' for descending order
                return 1;
            else
                return -1;
        }
    }

    Hand(Card[] handCards, Card[] comCards) {

        cards2 = new ArrayList<Card>(7);
        //build card array from hand and community
        for (Card card : comCards) {
            cards2.add(card);
        }
        for (Card card : handCards) {
            cards2.add(card);
        }

        cards2 = new ArrayList<Card>();
        cards2.sort(new CardComparator());

        //
        value = new int[6];

        int[] ranks = new int[14];
		int[] orderedRanks = new int[5];	 //miscellaneous cards that are not otherwise significant
		boolean straight=false, straightFlush=false;
		int sameCards = 1, sameCards2 = 1;
		int largeGroupRank = 0, smallGroupRank = 0;
		int index = 0, flushNr = 0;
		int topStraightValue = 0, topStraightFlushValue = 0, topFlushValue = 0;//arguably, we only need one.
        int[] flushCounter = new int[4];
        int flushSuite = 99;

        for (int x=0; x<=3; x++) {
			flushCounter[x]=0;
		}

        for (int x=0; x<=13; x++) {
			ranks[x]=0;
		}

        for (Card card : cards2) {//counts the nr of cards of same suits, and the nr of cards of same ranks
            ranks[card.getRank()]++;
            flushCounter[card.getSuit()]++;
        }

        if (ranks[1]==1) {//if ace, run this before because ace is highest card
			orderedRanks[index]=14;
			index++;
		}
		for (int x=13; x>=2; x--) {
			if (ranks[x]==1) {
				orderedRanks[index]=x;
				index++;
			}
		}

        for (int i = 0 ; i < 4; i++) {//check to see if we have at least one combination that results in flush
            if (flushCounter[i] > 4) {
                flushNr = flushCounter[i];//and stores the number of cards of same suite (5, 6 or 7)
                flushSuite = i;//and the suite
            }
        }

        if (flushNr > 0) {//if we have flush then
            int[] flushRanks = new int[14];//rank the flush cards
            index = 0;
            for (int i = 0; i < 14; i++) {
                flushRanks[i] = 0;
            }
            for (int i = 0; i < 7; i++) {
                if (cards2.get(i).getSuit() == flushSuite){
                    flushRanks[cards2.get(i).getRank()] = 1;
                    orderedRanks[index] = cards2.get(i).getRank();
                    index++;
                }
            }
            if (flushRanks[1] == 1) {
                for (int i = 6; i > 0; i--) {
                    orderedRanks[i] = orderedRanks[i-1];
                }
                orderedRanks[0] = 14;
            }
            //check for straight flush
            for (int x=1; x<=9; x++) {//can't have straight with lowest value of more than 10
			    if (flushRanks[x]==1 && flushRanks[x+1]==1 && flushRanks[x+2]==1 && flushRanks[x+3]==1 && flushRanks[x+4]==1) {
				    straightFlush=true;
				    topStraightFlushValue=x+4; //4 above bottom value
			    }
		    }
            if (flushRanks[10]==1 && flushRanks[11]==1 && flushRanks[12]==1 && flushRanks[13]==1 && flushRanks[1]==1) {//royal flush
			    straightFlush=true;
			    topStraightFlushValue=14; //higher than king
		    }
        }

        for (int x=1; x<=9; x++) {//can't have straight with lowest value of more than 10
			if (ranks[x]==1 && ranks[x+1]==1 && ranks[x+2]==1 && ranks[x+3]==1 && ranks[x+4]==1) {
				straight=true;
				topStraightValue=x+4; //4 above bottom value
				break;
			}
		}

		if (ranks[10]==1 && ranks[11]==1 && ranks[12]==1 && ranks[13]==1 && ranks[1]==1) {//ace high
			straight=true;
			topStraightValue=14; //higher than king
		}
        
        for (int x=13; x>=1; x--) {
			if (ranks[x] > sameCards) {
				if (sameCards != 1) {//if sameCards was not the default value
					sameCards2 = sameCards;
				    smallGroupRank = largeGroupRank;
                }
				sameCards = ranks[x];
				largeGroupRank = x;
			} else if (ranks[x] > sameCards2) {
				sameCards2 = ranks[x];
				smallGroupRank = x;
			}
		}

        //start hand evaluation
		if ( sameCards==1 ) {
			value[0]=1;
			value[1]=orderedRanks[0];
			value[2]=orderedRanks[1];
			value[3]=orderedRanks[2];
			value[4]=orderedRanks[3];
			value[5]=orderedRanks[4];
		}

		if (sameCards==2 && sameCards2==1)
		{
			value[0]=2;
			value[1]=largeGroupRank; //rank of pair
			value[2]=orderedRanks[0];
			value[3]=orderedRanks[1];
			value[4]=orderedRanks[2];
		}

		if (sameCards==2 && sameCards2==2) //two pair
		{
			value[0]=3;
			value[1]= largeGroupRank>smallGroupRank ? largeGroupRank : smallGroupRank; //rank of greater pair
			value[2]= largeGroupRank<smallGroupRank ? largeGroupRank : smallGroupRank;
			value[3]=orderedRanks[0];  //extra card
		}

		if (sameCards==3 && sameCards2!=2)
		{
			value[0]=4;
			value[1]= largeGroupRank;
			value[2]=orderedRanks[0];
			value[3]=orderedRanks[1];
		}

		if (straight && (flushNr == 0))
		{
			value[0]=5;
			value[1]=topStraightValue;
		}

		if ((flushNr > 0) && !straight)
		{
			value[0]=6;
			value[1]=orderedRanks[0]; //tie determined by ranks of cards
			value[2]=orderedRanks[1];
			value[3]=orderedRanks[2];
			value[4]=orderedRanks[3];
			value[5]=orderedRanks[4];
		}

		if (sameCards==3 && sameCards2==2)
		{
			value[0]=7;
			value[1]=largeGroupRank;
			value[2]=smallGroupRank;
		}

		if (sameCards==4)
		{
			value[0]=8;
			value[1]=largeGroupRank;
			value[2]=orderedRanks[0];
		}

		if (straight && (flushNr > 0))
		{
			value[0]=9;
			value[1]=topStraightValue;
		} 

    }

    void display() {
		String s;
		switch( value[0] ) {
			case 1:
				s="high card";
				break;
			case 2:
				s="pair of " + Card.rankAsString(value[1]) + "\'s";
				break;
			case 3:
				s="two pair " + Card.rankAsString(value[1]) + " " + Card.rankAsString(value[2]);
				break;
			case 4:
				s="three of a kind " + Card.rankAsString(value[1]) + "\'s";
				break;
			case 5:
				s=Card.rankAsString(value[1]) + " high straight";
				break;
			case 6:
				s="flush";
				break;
			case 7:
				s="full house " + Card.rankAsString(value[1]) + " over " + Card.rankAsString(value[2]);
				break;
			case 8:
				s="four of a kind " + Card.rankAsString(value[1]);
				break;
			case 9:
				s="straight flush " + Card.rankAsString(value[1]) + " high";
				break;
			default:
				s="error in Hand.display: value[0] contains invalid value";
		}
		//s = "				" + s;
		System.out.println(s);
	}

}
