import java.util.Arrays; 
import java.io.*; 
import java.util.*;
import java.util.Random;
//Project 1 by Jieyu Ren (jxr477)

class ReadingFromFile 
{//read from a txt file
  public static void main(String[] args) throws Exception 
  { 
    // pass the path to the file as a parameter 
      File file = new File("C:\\Users\\39601\\Desktop\\test.txt"); 
  
  BufferedReader br = new BufferedReader(new FileReader(file));
  
  EightPuzzle x = new EightPuzzle();
  String st; 
  while ((st = br.readLine()) != null)
  {
    if (st.equals("EightPuzzle x = new EightPuzzle();"))
    {}
    else if (st.equals("x.randomizeState(10);"))
      x.randomizeState(10);
    else if (st.equals("x.printState();"))
    {
      x.printState();
      System.out.println(" ");
    }
    else if (st.equals("x.solveAStar(\"h1\");"))
    {
      x.solveAStar("h1");
      System.out.println(" ");
    }
    else if (st.equals("x.solveAStar(\"h2\");"))
    {
      x.solveAStar("h2");
      System.out.println(" ");
    }
    else if (st.equals("x.solveBeam(4);"))
      x.solveBeam(4);
  }
}
}

class EightPuzzle
{
  char[] curStates = new char[9]; //the current state of the EightPuzzle
  char[] goalStates = new char[]{'b','1','2','3','4','5','6','7','8'}; //the goal state
  int maxNodes = 50;
 
  
  public void setState(String input) // set a state 
  {
    
    for(int i = 0; i < 3; i++)
      curStates[i] = input.charAt(i);
    
    for(int i = 3; i < 6; i++)
      curStates[i] = input.charAt(i + 1);
    
    for(int i = 6; i < 9; i++)
      curStates[i] = input.charAt(i + 2);
  }
  
  public void randomizeState(int steps) // Make n random moves from the goal state
  {
    Random randomno = new Random(); //set random seed    
    randomno.setSeed(10);
    setState("b12 345 678");
    int i = 0;
    while(i < steps)
    {
      boolean success = false;
      int direction = randomno.nextInt(4);
      if (direction  == 0)
      {
        success = move("up");
      }
      if (direction == 1)
      {
        success = move("down");
      }
      if (direction ==2 )
      {
        success = move("right");
      }
      if (direction == 3)
      {
        success = move("left");
      }
      if(success == true)
      {
        i = i + 1;
      }
    }
  }
  
  public void printState() //Print the current state.
  {
    for(int i = 0; i < 3; i++)
    {
      System.out.print(curStates[i]);
    }
    System.out.print(" ");
    for(int i = 3; i < 6; i++)
    {
      System.out.print(curStates[i]);
    }
    System.out.print(" ");
    for(int i = 6; i < 9; i++)
    {
      System.out.print(curStates[i]);
    }
    System.out.println(" ");
  }
  
  public boolean move(String direction) //Move the blank tile 'up', 'down', 'left', or 'right'.
  {
    int curlocation = this.getB();
    boolean success = false;
    if(direction.equals("up") && curlocation > 2)
    {
      char temp = curStates[curlocation - 3];
      curStates[curlocation - 3] = 'b';
      curStates[curlocation] = temp;
      success = true;
    }
    
    if(direction.equals("down") && curlocation < 6)
    {
      char temp = curStates[curlocation + 3];
      curStates[curlocation + 3 ] = 'b';
      curStates[curlocation] = temp;
      success = true;
    }
    
    if(direction.equals("right") && curlocation != 2 && curlocation != 5 && curlocation != 8)
    {
      char temp = curStates[curlocation + 1];
      curStates[curlocation + 1] = 'b';
      curStates[curlocation] = temp;
      success = true;
    }
    
    if(direction.equals("left") && curlocation != 0 && curlocation != 3 && curlocation != 6)
    {
      char temp = curStates[curlocation - 1];
      curStates[curlocation - 1] = 'b';
      curStates[curlocation] = temp;
      success = true;
    }
    
    return success;
  }
  
  public int getB() // find 'b'
  {
    int result = -1;
    for (int i = 0; i < 9; i++)
    {
      if (curStates[i] == 'b')
      {
        result = i;
      }
    }
    return result;
  }
  
  
  public void solveAStar(String heuristic) //solve the EightPuzzle using A-star
  {
    if (heuristic.equals("h1"))
    {
      ArrayList<EightPuzzle> allStates = new ArrayList<EightPuzzle>(); // using arraylist to store all curStates visited
      ArrayList<Integer> fofStates = new ArrayList<Integer>(); // using arraylist to store f-values of all curStates
      printState();
      int g = 0; // g values
      EightPuzzle x = new EightPuzzle();
      //add the starting state
      for(int i = 0; i < 9; i++)
      {
        x.curStates[i] = this.curStates[i];
      }
      allStates.add(x);
      fofStates.add(getDifference(x.curStates));
      while( Arrays.equals(this.curStates ,x.goalStates)== false && g < maxNodes) // continue looping until reach the goal state
      {
        g=g+1;
        allStates.remove(0);
        fofStates.remove(0);
        
        EightPuzzle checkUp = new EightPuzzle(); // test moving 'up' 'down' 'right' or 'left'
        for(int i = 0; i < 9; i++)
          checkUp.curStates[i] = this.curStates[i];
        
        EightPuzzle checkDown = new EightPuzzle();
        for(int i = 0; i < 9; i++)
          checkDown.curStates[i] = this.curStates[i];
        
        EightPuzzle checkLeft = new EightPuzzle();
        for(int i = 0; i < 9; i++)
          checkLeft.curStates[i] = this.curStates[i];
        
        EightPuzzle checkRight = new EightPuzzle();
        for(int i = 0; i < 9; i++)
          checkRight.curStates[i] = this.curStates[i];
        
        if(checkUp.move("up")) //prevent from coming back to the previous state
        {
          boolean contain = false;
          for(int i = 0; i < allStates.size(); i++)
          {
            if(Arrays.equals(allStates.get(i).curStates,checkUp.curStates))
            {
              contain = true;
            }
          }
          if(contain == false)
          {
            allStates.add(checkUp);
            fofStates.add(g + getDifference(checkUp.curStates));
          }
        }
        if(checkDown.move("down"))
        {
          boolean contain = false;
          for(int i = 0; i < allStates.size(); i++)
          {
            if(Arrays.equals(allStates.get(i).curStates,checkDown.curStates))
            {
              contain = true;
            }
          }
          if(contain == false)
          {
            allStates.add(checkDown);
            fofStates.add(g + getDifference(checkDown.curStates));
          }
        }
        if(checkLeft.move("left"))
        {
          boolean contain = false;
          for(int i = 0; i < allStates.size(); i++)
          {
            if(Arrays.equals(allStates.get(i).curStates,checkLeft.curStates))
            {
              contain = true;
            }
          }
          if(contain == false)
          {
            allStates.add(checkLeft);
            fofStates.add(g + getDifference(checkLeft.curStates));
          }
        }
        if(checkRight.move("right"))
        {
          boolean contain = false;
          for(int i = 0; i < allStates.size(); i++)
          {
            if(Arrays.equals(allStates.get(i).curStates,checkRight.curStates))
            {
              contain = true;
            }
          }
          if(contain == false)
          {
            allStates.add(checkRight);
            fofStates.add(g + getDifference(checkRight.curStates));
          }
        }
        
        for (int i = fofStates.size() - 1; i >= 0; i--) {
          for (int j = 1; j <= i; j++) {
            if (fofStates.get(j-1) > fofStates.get(j)) {
              int temp1 = fofStates.get(j-1);
              fofStates.set(j-1, fofStates.get(j));
              fofStates.set(j, temp1);
              EightPuzzle temp2 = new EightPuzzle();
              temp2 = allStates.get(j - 1);
              allStates.set(j - 1, allStates.get(j));
              allStates.set(j , temp2);
            }
          }
        }
        for(int i = 0; i < 9; i++)
        {
          this.curStates[i] = allStates.get(0).curStates[i];
        }
        printState();
      }
    }
    else if (heuristic.equals("h2")) //similar to h1 except the heuristic function used
    {
      ArrayList<EightPuzzle> allStates = new ArrayList<EightPuzzle>();
      ArrayList<Integer> fofStates = new ArrayList<Integer>();
      printState();
      int g = 0;
      EightPuzzle x = new EightPuzzle();
      for(int i = 0; i < 9; i++)
      {
        x.curStates[i] = this.curStates[i];
      }
      allStates.add(x);
      fofStates.add(getManDist(x.curStates));
      while( Arrays.equals(this.curStates ,x.goalStates)== false && g < maxNodes)
      {
        g=g+1;
        allStates.remove(0);
        fofStates.remove(0);
        EightPuzzle checkUp = new EightPuzzle();
        for(int i = 0; i < 9; i++)
        {
          checkUp.curStates[i] = this.curStates[i];
        }
        EightPuzzle checkDown = new EightPuzzle();
        for(int i = 0; i < 9; i++)
        {
          checkDown.curStates[i] = this.curStates[i];
        }
        EightPuzzle checkLeft = new EightPuzzle();
        for(int i = 0; i < 9; i++)
        {
          checkLeft.curStates[i] = this.curStates[i];
        }
        EightPuzzle checkRight = new EightPuzzle();
        for(int i = 0; i < 9; i++)
        {
          checkRight.curStates[i] = this.curStates[i];
        }
        if(checkUp.move("up"))
        {
          boolean contain = false;
          for(int i = 0; i < allStates.size(); i++)
          {
            if(Arrays.equals(allStates.get(i).curStates,checkUp.curStates))
            {
              contain = true;
            }
          }
          if(contain == false)
          {
            allStates.add(checkUp);
            fofStates.add(g + getManDist(checkUp.curStates));
          }
        }
        if(checkDown.move("down"))
        {
          boolean contain = false;
          for(int i = 0; i < allStates.size(); i++)
          {
            if(Arrays.equals(allStates.get(i).curStates,checkDown.curStates))
            {
              contain = true;
            }
          }
          if(contain == false)
          {
            allStates.add(checkDown);
            fofStates.add(g + getManDist(checkDown.curStates));
          }
        }
        if(checkLeft.move("left"))
        {
          boolean contain = false;
          for(int i = 0; i < allStates.size(); i++)
          {
            if(Arrays.equals(allStates.get(i).curStates,checkLeft.curStates))
            {
              contain = true;
            }
          }
          if(contain == false)
          {
            allStates.add(checkLeft);
            fofStates.add(g + getManDist(checkLeft.curStates));
          }
        }
        if(checkRight.move("right"))
        {
          boolean contain = false;
          for(int i = 0; i < allStates.size(); i++)
          {
            if(Arrays.equals(allStates.get(i).curStates,checkRight.curStates))
            {
              contain = true;
            }
          }
          if(contain == false)
          {
            allStates.add(checkRight);
            fofStates.add(g + getManDist(checkRight.curStates));
          }
        }
        
        for (int i = fofStates.size() - 1; i >= 0; i--) {
          for (int j = 1; j <= i; j++) {
            if (fofStates.get(j-1) > fofStates.get(j)) {
              int temp1 = fofStates.get(j-1);
              fofStates.set(j-1, fofStates.get(j));
              fofStates.set(j, temp1);
              EightPuzzle temp2 = new EightPuzzle();
              temp2 = allStates.get(j - 1);
              allStates.set(j - 1, allStates.get(j));
              allStates.set(j , temp2);
            }
          }
        }
        for(int i = 0; i < 9; i++)
        {
          this.curStates[i] = allStates.get(0).curStates[i];
        }
        printState();
      }
    }
  }
  
   public int getManDist(char[] input) //Find the Manhattan distance
 {
   int[] number = new int[9]; //convert char[] to int[]
   int manDist = 0;
    for (int a = 0; a < 9; a++)
    {
      if (input[a] != 'b')
      number[a] = Character.getNumericValue(input[a]);
      else
        number[a] = 0;
    }
    
  int index = -1;
  for (int y = 0; y < 3; y++)
  {
   for (int x = 0; x < 3; x++)
   {
    index++;
    if (number[index] != 0)
    {
     int horiz = number[index] % 3;
     int vert = number[index] / 3;
     manDist += Math.abs(vert - (y)) + Math.abs(horiz - (x));
    }
   }
  }
  return manDist;
}
  
public void curRandom(int steps)  // make n random moves from current state instead of from goal state
  {
    Random randomno = new Random(); //set random seed    
    randomno.setSeed(10);
    int i = 0;
    while(i < steps)
    {
      boolean success = false;
      int direction = randomno.nextInt(4);
      if (direction  == 0)
      {
        success = move("up");
      }
      if (direction == 1)
      {
        success = move("down");
      }
      if (direction ==2 )
      {
        success = move("right");
      }
      if (direction == 3)
      {
        success = move("left");
      }
      if(success == true)
      {
        i = i + 1;
      }
    }
  }
  
  public void solveBeam(int k)// solve the EightPuzzle using local beam search
  {
    ArrayList<EightPuzzle> path = new ArrayList<EightPuzzle>(); // store states and successor states in arraylist
    ArrayList<EightPuzzle> successors = new ArrayList<EightPuzzle>();
    int generated = 0;
    for(int b = 0; b < 100; b++)// genrate k successors
    {
      if(generated < k)
      {
        EightPuzzle y = new EightPuzzle();
        for(int a = 0; a < 9; a++)
        {
          y.curStates[a] = this.curStates[a];
        }
        y.curRandom(1);
        boolean contain = false;
        for(int i = 0; i < path.size(); i++)
        {
          if(Arrays.equals(path.get(i).curStates,y.curStates))
          {
            contain = true;
          }
        }
        if(contain == false)
        {
          path.add(y);
          generated++;
          y.printState();
        }
      }
    }
    boolean finished = false;
    int r = 0;
    while(finished == false && r < maxNodes)
    {
      r++;
      for(int q = 0; q < path.size(); q++)
          path.get(q).printState();
      for(int a = 0; a < path.size(); a++)
      {
        ArrayList<EightPuzzle> x = successor(path.get(0));
        for(int b = 0; b < x.size(); b++)
        {
          successors.add(x.get(b));
        }
      }
      path.clear();
      for(int c = 0; c < successors.size(); c++)
      {
        if(Arrays.equals(successors.get(c).curStates ,goalStates))
        {
          finished = true;
          this.curStates = successors.get(c).curStates;
          successors.get(c).printState();
        }
      }
      if(finished == false)
      {
        ArrayList<EightPuzzle> temp = new ArrayList<EightPuzzle>();
        ArrayList<Integer> value = new ArrayList<Integer>();
        for(int h = 0; h < successors.size(); h++)
        {
          value.add(getManDist(successors.get(h).curStates));
        }
        for (int z = successors.size() - 1; z >= 0; z--) {
          for (int p = 1; p <= z; p++) {
            if (value.get(p-1) > value.get(p)) {
              int temp1 = value.get(p-1);
              value.set(p-1, value.get(p));
              value.set(p, temp1);
              EightPuzzle temp2 = new EightPuzzle();
              temp2 = successors.get(p - 1);
              successors.set(p - 1, successors.get(p));
              successors.set(p , temp2);
            }
          }
        }
        for(int d = 0; d < k - 1; d++)
        {
          temp.add(successors.get(d));
        }
        successors.clear();
        for(int m = 0; m < temp.size();m++)
          path.add(temp.get(m));
        temp.clear();
        for(int q = 0; q < path.size(); q++)
          path.get(q).printState();
      }
    }
  }
  
  
  public ArrayList<EightPuzzle> successor(EightPuzzle input)  // return an array with all the successors of input state
  {
    ArrayList<EightPuzzle> successor = new ArrayList<EightPuzzle>();
    EightPuzzle checkUp = new EightPuzzle();
    for(int i = 0; i < 9; i++)
    {
      checkUp.curStates[i] = input.curStates[i];
    }
    EightPuzzle checkDown = new EightPuzzle();
    for(int i = 0; i < 9; i++)
    {
      checkDown.curStates[i] = input.curStates[i];
    }
    EightPuzzle checkLeft = new EightPuzzle();
    for(int i = 0; i < 9; i++)
    {
      checkLeft.curStates[i] = input.curStates[i];
    }
    EightPuzzle checkRight = new EightPuzzle();
    for(int i = 0; i < 9; i++)
    {
      checkRight.curStates[i] = input.curStates[i];
    }
    if(checkUp.move("up"))
    {
      successor.add(checkUp);
    }
    if(checkDown.move("down"))
    {
      successor.add(checkDown);
    }
    if(checkLeft.move("left"))
    {
      successor.add(checkLeft);
    }
    if(checkRight.move("right"))
    {
      successor.add(checkRight);
    }
    return successor;
  }  
  
  public int getDifference(char[] input)// find the differences between current state and goal state
  {
    int difference = 0;
    
    for(int i = 0; i < 9; i++)
    {
      if(input[i] != this.goalStates[i] && input[i] != 'b' )
        difference++;
    }
    return difference ;
  }
  
   public int getInvCount(char[] input)// find the number of inversions to check solvability
  {
    int[] number = new int[9];
    
    for (int a = 0; a < 9; a++)
    {
      if (input[a] != 'b')
      number[a] = Character.getNumericValue(input[a]);
      else
        number[a] = 0;
    }
    
    int invCount = 0;
    for (int i = 0; i < 8; i++)
    {
      for (int j = i + 1; j < 9; j++)
    {
        if (number[j] != 0)
        {
      if (number[i] > number[j])
      invCount++;
        }
    }
    }
    return invCount;
  }
  
  public boolean isSolvable(char[] input)// check whether a state is solvable
  {
    EightPuzzle x = new EightPuzzle();
    x.curStates = this.curStates;
    int invCount = x.getInvCount(input);
    if (invCount % 2 == 0)// if the number of inversions is even, then this state is solvable
      return true;
    else
      return false;
  }
  
  public int maxNodes(int input) //set maxNodes
  {
    maxNodes = input;
    return maxNodes;
  }
}