package assignment16;

import javax.swing.JOptionPane;
import java.io.*;
import java.util.*;

/**
 * Reads csv type file and displays using linked lists
 *
 * @author Igeta, David
 * @assignment ICS 211, Assignment #16
 * @date 3/10/2015
 */
public class IgetaDavid16 {
   /**
    * main() method begins program
    *
    * @param commandlineArguments is the filename inputted from user
    */
   public static void main(String[] commandlineArguments) {
      //person's name
      String sName = "sName";
      //person's age
      Integer iAge = new Integer(0);
      //name of file
      String sFileName = "sFileName";
      //scanner object to read file contents
      Scanner fileReader = null;
      //scanner object with userDelimiter() method
      Scanner tokenComma = null;
      //string object stores each line from file
      String sLine = "sLine";
      //creates new LinkedList object
      LinkedList list = new LinkedList();
      
      //checks if user entered a file name
      if(commandlineArguments.length != 1) {
         JOptionPane.showMessageDialog(null, "Please enter a file name in command line.");
         //terminates program
         System.exit(0);
      }
      sFileName = commandlineArguments[0];
      //creates new file object with the file name as a parameter
      File myFile = new File(sFileName);
      try {
         //links fileReader scanner object to the file object
         fileReader = new Scanner(myFile);
      }
      //throws if connection cannot be established
      catch(FileNotFoundException fnf) {
         JOptionPane.showMessageDialog(null, "File: " + sFileName + " was not found.");
         //terminates program
         System.exit(1);
      }
      //isolates first line of file - do not use
      sLine = fileReader.nextLine();
      try {
         //loops through each line of file
         while(fileReader.hasNextLine()) {
            //begins reading from second line in file
            sLine = fileReader.nextLine();
            //separates tokens with a comma
            tokenComma = new Scanner(sLine).useDelimiter(",");
            sName = tokenComma.next();
            iAge = Integer.parseInt(tokenComma.next());
            //creates a new person object for each line in list
            list.add(sName, iAge);
         }
         //displays result of all linked person objects
         JOptionPane.showMessageDialog(null, list.toString());
      }
      catch (NoSuchElementException nse) {
         //thrown when scanner object run into an empty space in file  
      }
      catch (NumberFormatException nfe) {
         System.out.println("ERROR: Cannot convert data to integer.");
      }
   }//end main() method
}//end IgetaDavid16 class

//****************************************************************************************

/* creates linked list of PersonNode objects */
class LinkedList {
   //instantiate PersonNode data field - stores address to object
   private PersonNode head = null;
   
   /**
    * constructor for LinkedList class data fields
    */
   public LinkedList() {
      //nothing, data fields already initialized
   }//end LinkedList constructor
   
   /**
    * adds a PersonNode to list
    * 
    * @param name is the person's name
    * @param age is the person's age
    */  
   public void add(String name, Integer age) {
   	//if the list of PersonNode objects is empty
      if (head == null) {
         //creates and assigns to toPerson
         head = new PersonNode(name, age, null);
      }
      //if the list is not empty
      else {
         //previous points to the head
         PersonNode previous = head;
         //current starts at second node in list
         PersonNode current = head.getNext();
         //while the next node is not null
         while (current != null) {
         	//cycles through PersonNode objects until end of list
            previous = current;
            current = current.getNext();
         }
         //creates a new node that has "null" for next.
         PersonNode newPerson = new PersonNode(name, age, null);
         //sets the PersonNode previous to new node
         previous.setNext(newPerson);
      }//end else
   }//end add() method
   
   /**
    * prints the list of PersonNode objects
    * @returns a string of concatenated sResults
    */   
   public String toString() {
   	//instantiate empty string
      String sResult = new String("");
   	//loop through all PersonNode objects in linked list
      for(PersonNode current = head; current != null; current = current.getNext()) {
      	//contatentes sResults until for loop is done
         sResult = sResult + current.toString() + "\n";
      }
      return sResult;
   }
}//end LinkedList class

//****************************************************************************************

/* stores data for each PersonNode object */
class PersonNode {
   //initialize three data fields
   private String personName;
   private Integer personAge;
   private PersonNode personNext;
   
   /**
    * constructor initializes data fields and creates objects
    * 
    * @param sName is the name of person
    * @param iAge is the age of person
    * @param pNext is the next object in queue 
    */
   public PersonNode(String sName, Integer iAge, PersonNode pNext) {
      personName = sName;
      personAge = iAge;
      personNext = pNext;  
   }//end PersonNode constructor
   
   /**
	 * displays data stored in each node.
	 * 
	 * @returns the printed results
	 */
   public String toString() {
      String sResult = personName + " is " + personAge + " years old.";
      return sResult;
   }//end toString() method
   
   /**
	 * accesses data from data field personNext.
	 * 
	 * @returns the data
	 */
   public PersonNode getNext() {
      return personNext;
   }//end getNext() method
   
   /**
	 * mutates or sets data in personNext data field.
	 * 
	 * @returns the new data
	 */
   public void setNext(PersonNode pNext) {
      personNext = pNext;
   }//end setNext() method

}//end PersonNode class

//****************************************************************************************