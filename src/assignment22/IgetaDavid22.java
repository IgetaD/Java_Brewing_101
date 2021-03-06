package assignment22;

import javax.swing.JOptionPane;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.File;

/**
 * A console menu for the IRS derived from Menu class
 *
 * @author Igeta, David
 * @assignment ICS 211, Assignment #22
 * @date 4/7/2015
 */
public class IgetaDavid22 {
// data fields
   private BinarySearchTree<Person> tree = new BinarySearchTree<Person>();
   private String database = new String("presidents.csv");

/** No-parameter constructor */
   public IgetaDavid22() {
   // We don't need any code in constructor,
   // as we already initialized the data fields.
   }

/**
 * Starts the menu.
 * 
 * @param args is not used           
 */
   public static void main(String[] args) {
   // Instantiate menu & display menu
      IgetaDavid22 menu = new IgetaDavid22();
      menu.displayMenu();
   }

/** Displays a menu, asks for choice, executes choice */
   public void displayMenu() {
   // read records from database and store in tree
      try {
         this.readFromDatabase();
      } 
      catch (Exception exception) {
         System.out.println("Error in reading from database: " + exception);
      }
   // list of choices
      String[] choices = { "Add Person", "Edit Person", "Remove Person",
            "Display Person", "Display List", "Exit Program",
            "Node Information" };
   // display loop
      int choice = 0;
      while (choice != choices.length) {
         choice = JOptionPane.showOptionDialog(null, // put in center of screen
            "Select a Command", // message to user
            "Shopping List Main Menu", // title of window
            JOptionPane.YES_NO_CANCEL_OPTION, // type of option
            JOptionPane.QUESTION_MESSAGE, // type of message
            null, // icon
            choices, // array of strings
            choices[choices.length - 1]); // default choice (last one)
         switch (choice) {
            case 0:
               try {
                  this.add();
               } 
               catch (Exception exception) {
                  // "null" puts message in center of screen
                  JOptionPane.showMessageDialog(null,
                        "Unable to add, because " + exception);
               }
               break;
            case 1:
               try {
                  this.edit();
               } 
               catch (Exception exception) {
                  JOptionPane.showMessageDialog(null,
                        "Unable to edit, because " + exception);
               }
               break;
            case 2:
               try {
                  this.remove();
               } 
               catch (Exception exception) {
                  JOptionPane.showMessageDialog(null,
                        "Unable to remove, because " + exception);
               }
               break;
            case 3:
               try {
                  this.displayOne();
               } 
               catch (Exception exception) {
                  JOptionPane.showMessageDialog(null,
                        "Unable to display, because " + exception);
               }
               break;
            case 4:
               this.displayAll();
               break;
            case 5:
            // quit the program
               choice = choices.length;
               break;
            case 6:
               try {
                  this.nodeInformation();
               } 
               catch (Exception exception) {
                  JOptionPane.showMessageDialog(null,
                        "Unable to display, because "
                        + exception);
               }
               break;
         }
      }
   // write records stored in tree to database
      try {
         this.writeToDatabase();
      } 
      catch (Exception exception) {
         System.out.println("Error in writing to database: " + exception);
         System.out.println("You might need to close the file.");
      }
   }

/** read from a database */
   private void readFromDatabase() throws FileNotFoundException {
   // Connect to the file in the current directory
      File file = new File(database);
      Scanner input = new Scanner(file); // may throw FileNotFoundException
   // read from file
      String line;
      String ssn;
      String tax;
      String name;
   // get rid of 1st line
      line = input.nextLine();
   // read contents of file and display on console
      while (input.hasNextLine()) {
         line = input.nextLine();
      // returns all characters up to a comma
         Scanner lineInput = new Scanner(line).useDelimiter(",");
         ssn = lineInput.next();
         tax = lineInput.next();
         name = lineInput.next();
         Person person = new Person(ssn, tax, name);
         tree.add(person);
      }
      input.close();
   }

/**
 * write Person records (objects) from tree and store in database.
 * 
 * @exception FileNotFoundException if cannot find the specified file            
 */
   private void writeToDatabase() throws FileNotFoundException {
   // make connection to database
      PrintWriter fileWriter = new PrintWriter(database);
   // write to file: title of each column (top row)
   // with NO spaces between commas (",")
      fileWriter.println("ssn,tax,name");
   // Write to file: all the data in the list.
   // We want PREORDER, so that when we read from the file again,
   // the tree will have the same structure as before.
   // If we use INORDER, then we will have an extremely unbalanced
   // tree (a linked list) when we read from the file again.
   // If we use POSTORDER, then we will have an unbalanced (but not
   // extremely unbalanced) tree when we read from the file again.
      String allData = tree.preOrder();
   // loop from 0 to length of string
      for (int i = 0; i < allData.length(); i++) {
      // isolate a single character
         String character = allData.substring(i, i + 1);
      // Since class PrintWriter will not create a newline in a file for
      // "\n",
      // we have to use println() instead of "\n"
         if (character.equals("\n")) {
            fileWriter.println();
         }
         // otherwise, we write all other characters to the file
         else {
            fileWriter.print(character);
         }
      }
   // don't forget to close the file connection!
   // may not write to file if not closed!
      fileWriter.close();
   }// end of writeToDatabase()

/** Prompt user for ssn, tax, and name */
   private void add() {
   // get user input
      String ssn = JOptionPane.showInputDialog("Enter person's ssn");
      String tax = JOptionPane.showInputDialog("Enter person's tax");
      String name = JOptionPane.showInputDialog("Enter person's name");
   // instantiate person & add
      Person person = new Person(ssn, tax, name);
      tree.add(person);
   }

/** Prompt user for person's ssn, then change to new tax */
   private void edit() {
   // get user input
   // displays message, and returns a string of text in text box
      String ssn = JOptionPane.showInputDialog("Enter person's ssn");
   // get user input
      String tax = JOptionPane.showInputDialog("Enter person's new tax");
   // get reference to real person using searchKey person
   // "searchKey" variable only contains the search key SSN
      Person searchKey = new Person(ssn);
      Person person = tree.get(searchKey); // no casting!!!
   // change the tax
      person.setTax(tax);
   }

/** Prompt user for person's ssn and remove */
   private void remove() {
   // get user input
   // displays message, and returns a string of text from text box
      String ssn = JOptionPane.showInputDialog("Enter person's ssn");
   // get reference to real person using searchKey person
   // "searchKey" variable only contains the search key SSN
      Person searchKey = new Person(ssn);
      tree.remove(searchKey);
   }

/** Prompt user for person's ssn, then display data for 1 person */
   private void displayOne() {
   // get user input
   // displays message, and returns a string of text in text box
      String ssn = JOptionPane.showInputDialog("Enter person's ssn");
   // get reference to real person using searchKey person
   // "searchKey" variable only contains the search key SSN
      Person searchKey = new Person(ssn);
      Person person = tree.get(searchKey); // no casting!!!
      if (person != null) {
         double tax = person.getTax();
         String name = person.getName();
      // make the output nice and pretty
         String output = new String("");
         output = output + "Name: " + name + "\n";
         output = output + "SSN:  " + ssn + "\n";
         output = output + "Tax: ";
         if (tax < 0) {
            tax = -tax;
            output = output + "-$" + tax + "\n";
         } 
         else {
            output = output + " $" + tax + "\n";
         }
         JOptionPane.showMessageDialog(null, output);
      } 
      else {
      // record does not exist
         JOptionPane.showMessageDialog(null, "ERROR: Record #" + ssn
            + " does not exist!");
      }
   }

/** Display */
   private void displayAll() {
      System.out.println("ssn,tax,name");
      System.out.println("------------");
      System.out.println(tree.toString());
   }

/**
 * Your programming assignment code goes in class BinarySearchTree in the
 * method "void nodeInformation(T searchKey)"
 */
   private void nodeInformation() {
   // get user input
      String ssn = JOptionPane.showInputDialog("Enter person's ssn");
      Person searchKey = new Person(ssn);
      tree.nodeInformation(searchKey);
   }

}// end of IgetaDavid22 class

//*******************************************************************************

/**
 * Generic class for a binary search tree
 * 
 * @author William McDaniel Albritton
 */
class BinarySearchTree<T extends java.lang.Comparable<T>> {

/**
 * displays information about selected node
 *
 * @param searchKey the item with the key value only          
 */
   public void nodeInformation(T searchKey) {
      //initialize three variables to be used in methods below
      T leftChildNode = null;
      T rightChildNode = null;
      String sNodeType = null;
      
      T person1 = this.get(searchKey);
      //prints out the current node entered by user 
      System.out.println("current node = " +  person1);
      //prints the type of node
      sNodeType = this.nodeType(searchKey);
      System.out.println(sNodeType);
      //if the node is of type root then print no parent
      if(sNodeType == "Type         = root") {
         System.out.println("Parent       = no parent");
      }
      //if not of type root then retrieve the parent node
      else {
         T parentNode = this.parentNode(searchKey);
         System.out.println("Parent       = " + parentNode);
      }
      //gets and displays the child nodes of the selected node
      try {
         leftChildNode = this.leftChildInfo(searchKey);
         rightChildNode = this.rightChildInfo(searchKey);
      }
      catch(NullPointerException npe) {
         //catches exception in case the left child node is null
      }   
      System.out.println("Left child   = " + leftChildNode);
      System.out.println("Right child  = " + rightChildNode);
      //skips a line between multiple entries
      System.out.print("\n");
   }//end nodeInformation() method

//data fields initialized here
   private BinaryNode<T> root = null;
   private boolean bRoot = true;
   private BinaryNode<T> parent = null;
/** constructor */
   public BinarySearchTree() {
   // data fields already initialized
   }
/**
 * gets the information abou the node
 * 
 * @param searchKey1 is an object storing only the search key           
 */
   public String nodeType(T searchKey1) {
   //cannot get direct access to the root outside the class
      return this.nodeType(root, bRoot, searchKey1);
   }

/**
 * displays selected node type
 * 
 * @param current is the root of the tree/subtree
 * @param bRoot determines if selected node is a the root            
 * @param searchKey2 is an object storing only the search key
 * @returns the type of node            
 */
   private String nodeType(BinaryNode<T> current, boolean bRoot, T searchKey2) {
      String sResult = "sResult";
      
      if (current == null) {
         throw new TreeException("ERROR: No such Person for " + searchKey2);
      }
      else {
      //if search key matches 
         if (searchKey2.compareTo(current.getData()) == 0) {
         boolean bLeft = true;
         boolean bRight = true;
            //determines if selected node has a left child
            try {
               current.getLeftChild().equals(null);
            }
            catch(NullPointerException npe) {
               bLeft = false;
            }
            //determines if selected node has a right child
            try {
               current.getRightChild().equals(null);
            }
            catch(NullPointerException npe) {
               bRight = false;
            }
            //if node does not have child nodes and is not the root
            if(bLeft == false && bRight == false && bRoot == false) {
               sResult = "Type         = leaf";
            }
            //if node is not the root and has at least one child node
            else if((bLeft == true || bRight == true) && bRoot == false) {
               sResult = "Type         = internal";
            }
            //if node is the root
            if(bRoot == true) {
               sResult = "Type         = root";
            }      
            return sResult;
         }
         //searches the left subtree if searchKey is less than the node, 
         else if (searchKey2.compareTo(current.getData()) < 0) {
            return this.nodeType(current.getLeftChild(), bRoot = false, searchKey2);
         }
         //searches the right subtree if searchKey is greater than the node,
         else {
            bRoot = false;
            return this.nodeType(current.getRightChild(), bRoot = false, searchKey2);
         }
      }//end else  
   }//end nodeType() method
   
/**
 * gets the parent node of selected node
 * 
 * @param searchKey1 is an object storing only the search key        
 */
   public T parentNode(T searchKey1) {
   //cannot get direct access to the root outside the class
      return this.parentNode(root, parent, searchKey1);
   }

/**
 * gets the parent node of selected node
 * 
 * @param current is the root of the tree/subtree 
 * @param parent acts as the selected node's parent           
 * @param searchKey2 is an object storing only the search key
 * @returns the parent node's data             
 */
   private T parentNode(BinaryNode<T> current, BinaryNode<T> parent, T searchKey2) {
      if (current == null) {
         throw new TreeException("ERROR: No such Person for " + searchKey2);
      }
      else {
      //if the search key matches, return the item's address
         if (searchKey2.compareTo(current.getData()) == 0) {
            return parent.getData();
         }
         //searches the left subtree if searchKey is less than the node, 
         else if (searchKey2.compareTo(current.getData()) < 0) {
            //assigns current node to parent node before current.getLeftChild()
            return this.parentNode(current.getLeftChild(), parent = current, searchKey2);
         }
         //searches the right subtree if searchKey is greater than the node,
         else {
            //assigns current node to parent node before current.getRightChild()
            return this.parentNode(current.getRightChild(), parent = current, searchKey2);
         }
      }//end else
   }//end parentNode() method
   
/**
 * gets the left child node data of selected node
 * 
 * @param searchKey1 is an object storing only the search key           
 */
   public T leftChildInfo(T searchKey1) {
   //cannot get direct access to the root outside the class
      return this.leftChildInfo(root, searchKey1);
   }

/**
 * gets the left child node data of selected node
 * 
 * @param current is the root of the tree/subtree            
 * @param searchKey2 is an object storing only the search key
 * @returns the left child node's data            
 */
   private T leftChildInfo(BinaryNode<T> current, T searchKey2) {
      if (current == null) {
         throw new TreeException("ERROR: No such Person for " + searchKey2);
      }
      else {
      //if the search key matches, return the item's address
         if (searchKey2.compareTo(current.getData()) == 0) {
            //assigns the left child node to current node
            current = current.getLeftChild();
            return current.getData();
         }
         //searches the left subtree if searchKey is less than the node, 
         else if (searchKey2.compareTo(current.getData()) < 0) {
            return this.leftChildInfo(current.getLeftChild(), searchKey2);
         }
         //searches the right subtree if searchKey is greater than the node,
         else {
            return this.leftChildInfo(current.getRightChild(), searchKey2);
         }
      }//end else
   }//end leftChildInfo() method
   
/**
 * gets the right child node's data of selected node
 * 
 * @param searchKey1 is an object storing only the search key           
 */
   public T rightChildInfo(T searchKey1) {
   // cannot get direct access to the root outside the class
      return this.rightChildInfo(root, searchKey1);
   }

/**
 * gets the right child node's data of selected node
 * 
 * @param node is the root of the tree/subtree            
 * @param searchKey2 is an object storing only the search key            
 */
   private T rightChildInfo(BinaryNode<T> current, T searchKey2) {
      if (current == null) {
         throw new TreeException("ERROR: No such Person for " + searchKey2);
      }
      else {
      //if the search key matches, return the item's address
         if (searchKey2.compareTo(current.getData()) == 0) {
            //assigns the right child node of current node to current
            current = current.getRightChild();
            return current.getData();
         }
         //searches the left subtree if searchKey is less than the node, 
         else if (searchKey2.compareTo(current.getData()) < 0) {
            return this.rightChildInfo(current.getLeftChild(), searchKey2);
         }
         //searches the right subtree if searchKey is greater than the node,
         else {
            return this.rightChildInfo(current.getRightChild(), searchKey2);
         }
      }//end else
   }//end rightChildInfo() method
   
/**
 * adds an item to the tree
 * 
 * @param item is the object to be added           
 */
   public void add(T item) {
   // calls a recursive, private method
   // cannot get direct access to the root outside the class
      root = add(root, item);
   }

/**
 * adds an item to the tree
 * 
 * @param node is the root of the tree/subtree          
 * @param item is the object to be added           
 */
   private BinaryNode<T> add(BinaryNode<T> node, T item) {
   // base case: empty tree or end of a leaf
      if (node == null) {
         return new BinaryNode<T>(item, null, null);
      }
      // base case: duplicate node, so throw exception
      else if (item.compareTo(node.getData()) == 0) {
         throw new TreeException("No duplicate items are allowed!");
      }
      // recursive case: if item is less than current node
      // then move to left child node
      else if (item.compareTo(node.getData()) < 0) {
      // set the node's left child to the
      // left subtree with item added
         node.setLeftChild(this.add(node.getLeftChild(), item));
         return node;
      }
      // recursive case: if item is greater than current node
      // then traverse to right child node
      else {
      // set the node's right child to the
      // right subtree with item added
         node.setRightChild(this.add(node.getRightChild(), item));
         return node;
      }
   }

/**
 * called automatically by println/print method
 * 
 * @return an inorder String of the tree
 */
   public String toString() {
      return this.inOrder(root);
   }

/**
 * inOrder display of nodes, with newline between each node
 * 
 * @param node is the root of the tree/subtree        
 * @return an inorder String of the tree
 */
   private String inOrder(BinaryNode<T> node) {
      String displayNodes = "";
      if (node != null) {
         displayNodes = displayNodes + 
            this.inOrder(node.getLeftChild());
         displayNodes = displayNodes + node.toString() + "\n";
         displayNodes = displayNodes + 
            this.inOrder(node.getRightChild());
      }
      return displayNodes;
   }

/**
 * preOrder traversal
 * 
 * @return an preOrder String of the tree
 */
   public String preOrder() {
      return this.preOrder(root);
   }

/**
 * preOrder display of nodes, with newline between each node
 * 
 * @param node is the root of the tree/subtree          
 * @return an preOrder String of the tree
 */
   private String preOrder(BinaryNode<T> node) {
      String displayNodes = "";
      if (node != null) {
         displayNodes = displayNodes + node.toString() + "\n";
         displayNodes = displayNodes + 
            this.preOrder(node.getLeftChild());
         displayNodes = displayNodes + 
            this.preOrder(node.getRightChild());
      }
      return displayNodes;
   }

/**
 * postOrder traversal
 * 
 * @return an postOrder String of the tree
 */
   public String postOrder() {
      return this.postOrder(root);
   }

/**
 * postOrder display of nodes, with newline between each node
 * 
 * @param node is the root of the tree/subtree          
 * @return an postOrder String of the tree
 */
   private String postOrder(BinaryNode<T> node) {
      String displayNodes = "";
      if (node != null) {
         displayNodes = displayNodes + this.postOrder(node.getLeftChild());
         displayNodes = displayNodes + this.postOrder(node.getRightChild());
         displayNodes = displayNodes + node + "\n";
      }
      return displayNodes;
   }

/**
 * gets an item from the tree with the same search key
 * 
 * @param searchKey1 is an object storing only the search key           
 */
   public T get(T searchKey1) {
   // cannot get direct access to the root outside the class
      return this.get(root, searchKey1);
   }

/**
 * gets an item from the tree with the same search key
 * 
 * @param node is the root of the tree/subtree            
 * @param searchKey2 is an object storing only the search key            
 */
   private T get(BinaryNode<T> node, T searchKey2) {
   // if not found, throw exception
      if (node == null) {
         throw new TreeException("Item not found!");
      } 
      else {
      // if the search key matches, return the item's address
         if (searchKey2.compareTo(node.getData()) == 0) {
            return node.getData();
         }
         // if the search key of the searchKey is less than the node,
         // then search the left subtree
         else if (searchKey2.compareTo(node.getData()) < 0) {
            return this.get(node.getLeftChild(), searchKey2);
         }
         // if the search key of the searchKey is greater than the node,
         // then search the right subtree
         else {
            return this.get(node.getRightChild(), searchKey2);
         }
      }
   }

/**
 * Calls a recursive method that removes an item from the tree with the same
 * search key
 * 
 * @param searchKey3 is an object storing only the search key            
 */
   public void remove(T searchKey3) {
      root = this.remove(root, searchKey3);
   }

/**
 * Finds the item to be removed from the tree with the same search key
 * 
 * @param node is the root of the tree/subtree
 *            
 * @param searchKey4 is an object storing only the search key            
 */
   private BinaryNode<T> remove(BinaryNode<T> node, T searchKey4) {
   // if item not found, throw exception
      if (node == null) {
         throw new TreeException("Item not found!");
      }
      // if search key is less than node's search key,
      // continue to left subtree
      else if (searchKey4.compareTo(node.getData()) < 0) {
         node.setLeftChild(this.remove(node.getLeftChild(), searchKey4));
         return node;
      }
      // if search key is greater than node's search key,
      // continue to right subtree
      else if (searchKey4.compareTo(node.getData()) > 0) {
         node.setRightChild(this.remove(node.getRightChild(), searchKey4));
         return node;
      }
      // found node containing object with same search key,
      // so delete it
      else {
      // call private method remove
         node = this.remove(node);
         return node;
      }
   }

/**
 * Removes leaf nodes, and nodes with one child node from the tree with the
 * same search key & removes 2 child nodes too
 * 
 * @param node is the root of the tree/subtree            
 */
   private BinaryNode<T> remove(BinaryNode<T> node) {
   // if node is a leaf,return null
      if (node.getLeftChild() == null && node.getRightChild() == null) {
         return null;
      }
      // if node has a single right child node,
      // then return a reference to the right child node
      else if (node.getLeftChild() == null) {
         return node.getRightChild();
      }
      // if node has a single left child node,
      // then return a reference to the left child node
      else if (node.getRightChild() == null) {
         return node.getLeftChild();
      }
      // if the node has two child nodes
      else {
      // get next Smaller Item, which is Largest Item in Left Subtree
      // The next Smaller Item is stored at the rightmost node in the left
      // subtree.
         T largestItemInLeftSubtree = this.getItemWithLargestSearchKey(node
            .getLeftChild());
      // replace the node's item with this item
         node.setData(largestItemInLeftSubtree);
      // delete the rightmost node in the left subtree
         node.setLeftChild(this.removeNodeWithLargestSearchKey(node
            .getLeftChild()));
         return node;
      }
   }

/**
 * Returns the address of the item with the largest search key in the tree
 * 
 * @param node is the root of the tree/subtree            
 */
   private T getItemWithLargestSearchKey(BinaryNode<T> node) {
   // if no right child, then this node contains the largest item
      if (node.getRightChild() == null) {
         return node.getData();
      }
      // if not, keep looking on the right
      else {
         return this.getItemWithLargestSearchKey(node.getRightChild());
      }
   }

/**
 * Removes the node with the largest search key
 * 
 * @param node is the root of the tree/subtree            
 */
   private BinaryNode<T> removeNodeWithLargestSearchKey(BinaryNode<T> node) {
   // if no right child, then this node contains the largest item
   // so replace it with its left child
      if (node.getRightChild() == null) {
         return node.getLeftChild();
      }
      // if not, keep looking on the right
      else {
         node.setRightChild(this.removeNodeWithLargestSearchKey(node
            .getRightChild()));
         return node;
      }
   }

/**
 * Driver code to test class
 * 
 * @param commandlineArguments are not used           
 
   public static void main(String[] commandlineArguments) {
   // using BinaryNode<String>
      BinarySearchTree<String> tree = new BinarySearchTree<String>();
      System.out.println("TEST add() method:");
      tree.add("mahimahi");
      tree.add("hee");
      tree.add("ono");
      tree.add("mano");
      tree.add("lauwiliwili");
      tree.add("honu");
      tree.add("ulua");
      tree.add("uhu");
      tree.add("ahi");
      System.out.println("preorder:\n" + tree.preOrder());
      System.out.println("inorder:\n" + tree.toString());
      System.out.println("postorder:\n" + tree.postOrder());
   
   // test get
      System.out.println("TEST get() method:");
      String fish = tree.get("mahimahi");
      System.out.println(fish);
      fish = tree.get("ono");
      System.out.println(fish);
      try {
         fish = tree.get("tuna");
         System.out.println(fish);
      } 
      catch (TreeException exception) {
         System.out.println(exception.toString());
      }
      fish = tree.get("uhu");
      System.out.println(fish);
   
   // test remove
      System.out.println("\nTEST remove() method:");
      System.out.println("inorder:\n" + tree.toString());
      tree.remove("mahimahi");
      System.out.println("inorder:\n" + tree.toString());
      tree.remove("ulua");
      System.out.println("inorder:\n" + tree.toString());
      tree.remove("ono");
      System.out.println("inorder:\n" + tree.toString());
      tree.remove("ahi");
      System.out.println("inorder:\n" + tree.toString());
   }// end of main
   */
}// end of BinarySearchTree class

// ********************************************************************

/** For use with the BinarySearchTree class */
class TreeException extends RuntimeException {
/**
 * @param message is the cause of the error            
 */
   public TreeException(String message) {
      super(message);
   }
}// end of class
