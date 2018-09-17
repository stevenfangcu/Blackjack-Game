Steven Fang 101014469 
Comp3004 Individual Assignment 
how to run: go to blackjackGui and run it on a javafx environment with 
maven project dependencies. Have the user input start or the name of a textfile to be used for the game status.
***IMPORTANT***
The input file must be in a .txt format as well as “ “ as an indicator between cards and actions. Another thing to add is “S” is to stand, “D” is to split and “H” to hit.
For the fourth given test case instead of “SK HK CQ D9 D H6 C5 H D3 S H D5 S” 
in our case would be “SK HK CQ D9 D H H6 H C5 H D3 S H D5 S “. When splitting the hits or “H” are alternated between the two hands. In this specific case it would from 
(10,10), Hit for H6 , (16,10), Hit for C5, (16,15), Hit for D3, (19,15), Stand, Hits for D5, (19,20), Stand. and the dealer has to stand on 19.
