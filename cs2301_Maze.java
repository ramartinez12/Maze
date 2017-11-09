package Maze;
/*
 * Course: Data Structure CS2302
 * Author: Roman Martinez
 * Objective: to practice using disjoint set forests as a way to generate a path in the maze that was being created 
 * Instrutor: Olac Fuentes
 * T.A: Saiful Abu
 * Date Last Modified: November 19, 2015
 * Purpose: the purpose of the program is to use disjoint set forests to create a maze with having guaranteed a path from one red dot to the other 
 */
import java.util.*;
public class cs2301_Maze {
   private int N;                 // dimension of maze
   private boolean[][] north;     // is there a wall to north of cell i, j
   private boolean[][] east;
   private boolean[][] south;
   private boolean[][] west;
   private int[] S;               // this is the array that holds the disjoint set forest
   private int numberOfSets;      // this counts the number of sets and is used in the while loop to hold the number of sets
   private Random r;			  // this will be used to generate the random cell and the random side of the cell
 
   public cs2301_Maze(int N) {
	   // constructor for the maze
       this.N = N;
       StdDraw.setXscale(0, N+2);
       StdDraw.setYscale(0, N+2);
       init();
   }

   private void init() {
      // initialze all walls as present
		 // notice that each wall is stored twice 
       north = new boolean[N+2][N+2];
       east  = new boolean[N+2][N+2];
       south = new boolean[N+2][N+2];
       west  = new boolean[N+2][N+2];
       S = new int[N*N];
       // the disjoint set forest is created
       r = new Random();
       // random generator is created and initialized to r
       for (int x = 0; x < N+2; x++)
           for (int y = 0; y < N+2; y++)
               north[x][y] = east[x][y] = south[x][y] = west[x][y] = true;
       // the for loop will initialized the sets in the array
       for(int i = 0;i<S.length;i++){
    	   S[i]=-1;
    	   // every time the set is created the numberOfSets is added one
    	   numberOfSets++;
       }
       }
   
   public void createMaze(){
	   // this method is used to generate the random paths for the set disjoint forest
	   while(numberOfSets>1){
		   // inside the while loop os what generates the random choosing of the walls that are going to be removed
		   int random = (int)(Math.random()*S.length);
		   //generates the random value of the cell being manipulated
		   int y = (random/N)+1;
		   // this will get the y coordinate of the value of the random selected cell in the array that holds the wall
		   int x = (random-((y*N)-N))+1;
		   // this will get the x coordinate of the value of the random selected cell in the array that holds the wall
		   int randomSide = (int)(Math.random()*4);
		   // randomly generates the side that is going to be removed
		   int secondCell;
		   // this will be the cell that is connected to the original cell
		   char side;
		   // this holds the character of the side that was randomly selected
		   
		   // the if are base cases that are based on the side that was chosen randomly and what the value of x coordinate or y coordinate is
		   if((randomSide==0) && (y<N)){
			   side = 'N';
			   secondCell=random+N;
			   // the secondcell value is determined by adding N to the original cell
			   unionAndRemove(S,random,secondCell,x,y,side);
			   // union and remove on the original and secondCell
		   }
		   else if((randomSide==1) && (x<N)){
			   side = 'E';
			   secondCell=random+1;
			   // the secondcell value is determined by adding 1 to the original cell
			   unionAndRemove(S,random,secondCell,x,y,side);
			   // union and remove on the original and secondCell
		   }
		   else if((randomSide==2)&& (y>1)){
			   side = 'S';
			   secondCell=random-N;
			   // the secondcell value is determined by subtracting N to the original cell
			   unionAndRemove(S,random,secondCell,x,y,side);
			   // union and remove on the original and secondCell
		   }
		   else if((randomSide==3)&& (x>1)){
			   side = 'W';
			   secondCell=random-1;
			   // the second cell value is determined by subtracting 1 to the original cell
			   unionAndRemove(S,random,secondCell,x,y,side);
			   // union and remove on the original and secondCell
		   }
	   }
   }
   
   public void unionAndRemove(int[] A, int a, int b, int x, int y, char c){
	   // the method is used to unite the sets if they are different and to removed the side at the cell that was randomly selected
	   int ra = find(A,a);
	   int rb = find(A,b);
	   if(ra!=rb){
		   numberOfSets--;
		   // subtracts one if union is complete
		   A[rb] = ra;
		   remove(x,y,c);
		   // removes the wall at the x and y coordinates
	   }
	   S=A;
	   // this is to set the temporary array to the set disjoint forest original array 
   }
   
   public int find(int[] A, int a){
	   // method is used to find the index of the root in the set for union
	   if(A[a]<0)
		   return a;
	   else
		   return find(A,A[a]);
   }

   public void remove(int x, int y, char d){
	 	int origin = (y-1)*N+(x-1);
		int dest;
		
	 	if ((d=='N') && (y<N)){
			dest = origin+N;
			north[x][y] = south[x][y+1] = false;
		}
		else if ((d=='W')&& (x>1)){
			dest = origin-1;
			west[x][y] = east[x-1][y] = false;
     }
		else if ((d=='S')&& (y>1)){
			dest = origin-N;
			south[x][y] = north[x][y-1] = false;
		}
		else if ((d=='E')&& (x<N)){
			dest = origin+1;
			east[x][y] = west[x+1][y] = false;		
		}
	 }	
   
   public void printSet(){
	   // this method is to print the completed set after the maze was created 
	   for(int i=0;i<S.length;i++){
		   System.out.print(S[i]+" ");
	   }
   }
	 
	 // display the maze 
   public void draw() {
       StdDraw.setPenColor(StdDraw.RED);
       StdDraw.filledCircle(N + 0.5, N + 0.5, 0.375);
       StdDraw.filledCircle(1.5, 1.5, 0.375);

       StdDraw.setPenColor(StdDraw.BLACK);
       for (int x = 1; x <= N; x++) {
           for (int y = 1; y <= N; y++) {
               if (south[x][y]) StdDraw.line(x, y, x + 1, y);
               if (north[x][y]) StdDraw.line(x, y + 1, x + 1, y + 1);
               if (west[x][y])  StdDraw.line(x, y, x, y + 1);
               if (east[x][y])  StdDraw.line(x + 1, y, x + 1, y + 1);
           }
       }
       StdDraw.show(0);
   }
	 
   public static void main(String[] args) {
	   Scanner read = new Scanner(System.in);
	   // asks the user for the dimension desired for the maze
	   System.out.println("Please enter the size you want the maze");
	   int N = read.nextInt();
	   cs2301_Maze maze = new cs2301_Maze(N);
       StdDraw.show(0);
       maze.createMaze();
       maze.draw();
   }
}
