
import java.io.File;
import java.util.Scanner;

public class Hashing_0990 {
	
	static int[] T= {};
	static char[] A={};
	static int hasTable = 0; //  for the flag
	static int InsertingIndex=1;
	
	static int[] tempT = {};
	static char[] tempA = {};
	static char[] reTempCharTable = {};
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
		/*	System.out.println("Please enter the proper File name with extension");
			Scanner sc  = new Scanner(System.in);
			String filename = sc.nextLine();*/
			
			if(args.length!=1) {
				System.out.println("Please enter the proper File Name");
				return;
			}
			
			File file = new File(args[0]);
			//File file = new File("TestFile.txt");
			Scanner scOperations= new Scanner(file);
			Scanner scData= new Scanner(file);
			int operationLength = 0; 
			
			//to get the total number of operations (rows counts)
			while(scOperations.hasNextLine()){
				operationLength++;
				scOperations.nextLine();
			}
			
			System.out.println("Operation length/ File size = " + operationLength);
			
			String[] operations = new String[operationLength]; // to store the opeartion , arg 1 of file
			String[] wordsList = new String[operationLength]; // to store the strings  , arg 2 of file
			
			String[] parts = new String[2]; // for each row operations in a file
			int operationCouter = 0; //couters
			int wordCounter = 0;
			int rowNumber = 1;
			
			
			// storing the operation and words in the arrays from fileData
			while(scData.hasNextLine()){
				String currentRow = scData.nextLine();
				parts = currentRow.split(" ", 2);
				
				//System.out.println( rowNumber + " :- " +  parts[0] + " " + parts[1]);
				//int part0 = Integer.parseInt(parts[0]);
				
				//System.out.println(part0);
				if(parts[0].equals("14")){ // to define the size off hash table T
					boolean flag = true;
					int x = 0;
					//String error="";
					try {
						x = Integer.parseInt(parts[1]);
					} catch (Exception e) {
						flag = false; // if size is not a valid integer
						//error = String(e);
					}
					
					if (flag == true) {
						if(x>0) {
							operations[operationCouter] = parts[0];
							wordsList[wordCounter] = parts[1];
							operationCouter++;
							wordCounter++;
						}else {
							System.out.println("Hash Table size can not be less than 1, please provide the valid size in first argument");
						}
						
					}else {
						System.out.println("Invalid table size  at row number = " + rowNumber );
					}

				} else if ((parts[0].equals("10")) || (parts[0].equals("11")) || (parts[0].equals("12")) || (parts[0].equals("15"))  ){  // 10 = insert, 11 = delete , 12 = search, 15 = comment
					operations[operationCouter] = parts[0];
					wordsList[wordCounter] = parts[1];
					operationCouter++;
					wordCounter++;
				}else if( (parts[0].equals("13"))){ // this is to print the structure
					operations[operationCouter] = parts[0];
					wordsList[wordCounter] = null;
					operationCouter++;
					wordCounter++;
				}else{
					System.out.println("Invalid operation/command at rowNumber "+ rowNumber + " of the file ");
				}
				
				rowNumber++;
			}
			
/*			System.out.println("---------------File-------------");
			//temp loop , have to remove
			for(int i = 0 ; i < operations.length; i++){
				System.out.println(operations[i] + " " + wordsList[i]);
			}
			System.out.println("------------------------------");*/
			
			//-------------------------------------------------------------------
			// 10 is for Insert,parameter is String
			// 11 is for deletion,parameter is String
			// 12 is for search,parameter is String
			// 13 if for print, parameter is empty , null
			// 14 is for create,parameter is integer(have to convert from string )
			// 15 is comment,ignore the content of this line.
			
			
			//On the basic of operation, stored in the array and do the functionality
			for(int i= 0 ; i < operations.length; i++){
				
				if(operations[i].equals("14")){ // first row should be the size of the table 
					for (int j = i; j < operations.length; j++) {
						if (operations[j] == null) {
							// System.out.println("empty row");
						} else if (operations[j].equals("10")) {
							insertData(wordsList[j]); // passing the name
						} else if (operations[j].equals("11")) {
							delete(wordsList[j]);
						} else if (operations[j].equals("12")) {
							search(wordsList[j]);
						} else if (operations[j].equals("13")) {
							printArray();
						}else if (operations[j].equals("14") && hasTable == 0) {// the flag tells wheather the table is created or not
							hasTable = 1; //table is created
							createHashTable(wordsList[j]); // passing the size of hash table array
						} else if (operations[j].equals("15")) {
							System.out.println("Command 15, Please ignore it");
						} else if ( hasTable == 1) {
							System.out.println("Table is already created, duplicate cmd for table creation");
						}else {
							System.out.println("This is not a valid operation");
						}
						
					}
					break;
				
				}else if(operations[i] == null){
					// do nothing
					System.out.println("Table size can't be null");
				}else {
					System.out.println("Table is  not created yet ,So can't insert the data");
				}
			}
			
			//sc.close();
			scData.close();
			scOperations.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println( e );
		}
		
		
	}
	
	//---------------------------------------------------------------------------------------------------------
	// creating and initizitig the arrays
	public static void createHashTable(String size){
		int s = Integer.parseInt(size);
		T = new int[s];
		A = new char[15 * s]; // acc to prp description
		
		//initizlise the empty array A of words with space
		for(int i = 0 ; i <A.length;i++){
			A[i] = ' ';
		}
		
		// initize the table structure with -1
		for(int i =0 ; i <T.length ; i++){
			T[i] = -1;
		}
	}
	
	
	
	//-------------------------------------------------------------------------------------------------------------------
	//inserting the data in A and T arrays
	public static void insertData(String name){

		insertInA(name);
		insertInT(name);
		
	}
	
	
	// inserting the name in A
	public static void insertInA(String name){
		
		if (checkCollision(name) == false) { // if collsion occur then we have to increase the size of both the arrays
			reSize();
		}
		
		//again check for the collision after resizing the array A length;
		if (checkCollision(name) == false) {
			reSize();
		}
		
		if(checkCollision(name) == true){
			
			//getting the inserting position
			for (int i = 0; i < A.length; i++) {
				if ((int) A[i] == 32) { // checking the space , where ever is te first space come insert from there
					InsertingIndex = i;
					break;
				}
			}
			
			for (int j = InsertingIndex,k= 0; j < (InsertingIndex + name.length()); j++) {
				if (j >= A.length) {
					// have to increase the length of A (doubling its size)
					StructCharArray_A(name);
				}
				A[j] = name.charAt(k);
				k++;
			}
			
			if ((InsertingIndex + name.length()) >= A.length) { // checking the size of A, after insertion
				StructCharArray_A(name);
			}
			
			A[InsertingIndex + name.length()] = '\0';// adding  \O after each word, means word terminates
		}
		
	}
	
	
	// This is to check if the collision occurs.
		public static boolean checkCollision(String fullName) {

			int Sum = 0,hdash = 0,hashFunc = 0;
			

			for (int i = 0; i < fullName.length(); i++) {
				Sum = Sum + ((int) fullName.charAt(i));
			}
			// System.out.println(hashSum);
			//have to decrement the sum by 2 according to prp description
			hdash = (Sum-2) % T.length;
			

			for (int j = 0; j < T.length; j++) {
				hashFunc = (hdash + (j * j)) % T.length;
				// System.out.println(hashFunc);
				if (T[hashFunc] == -1) {
					return true;
				}
			}
			return false;
		}

		
		// This will Increase the size of the character array (A) if it becomes full.
		public static void StructCharArray_A(String reName) {
	
			 tempA = new char[2 * A.length];  // making new temp array to increase the size of A and then copy items of A again
			for (int i = 0; i < tempA.length; i++) {
				tempA[i] = ' ';
			}

			for (int i = 0; i < A.length; i++) {
				tempA[i] = A[i];
			}

			A = tempA; // coping all the item back to A

		}
		
		
		
		// in this, making increasing the size of both the arrays and agaon assiging the values .
		public static void reSize() {
			int insertIndex = 0;
			int totalSum = 0;
			tempT = new int[2 * T.length];
			tempA = new char[2 * A.length];
			
			for (int i = 0; i < tempA.length; i++) {
				tempA[i] = ' ';
			}

			for (int j = 0; j < tempT.length; j++) {
				tempT[j] = -1;
			}

			for (int i = 0; i < A.length; i++) {
				tempA[i] = A[i];
			}

			A = tempA;

			for (int k = 0; k < A.length; k++) {
				// System.out.println(A[k]);
				if ((int) A[k] != 42) { // check for the delete items (*)
					if ((int) A[k] == 0) { //end of first word
						// System.out.println("insert " + insertIndex);
						// System.out.println("Sum is " + totalSum);
						// System.out.println("index " + k);
						if (totalSum != 0) {
							reEnterData(totalSum, tempT.length, insertIndex); // insertt in TempT
						}
						insertIndex = k + 1; // this will point the value of insertIndex to the next word
						totalSum = 0; //  start again
						continue;
					}
					totalSum = totalSum + (int) A[k];
				}
				if ((int) A[k] == 32) { // end of the name , if space came
					break;
				}

			}
			T = tempT;

		}
		
		
		
		// this is reentering the data into the integer array
		public static void reEnterData(int totalSum, int totalLength, int insertIndex) {

			int reFunc = 0;
			int rehashFunc = 0;

			//reFunc = totalSum % totalLength;
			reFunc = (totalSum-2) % totalLength;

			for (int j = 0; j < tempT.length; j++) {
				rehashFunc = (reFunc + (j * j)) % totalLength;
				if (tempT[rehashFunc] == -1) {
					tempT[rehashFunc] = insertIndex; // inserting the starting pointer of A in new Temp T array
					break;
				}
			}

		}
	
	
	
		// This function will insert starting index in T.
		public static void insertInT(String fulName) {
			
			int Sum = 0,hdash = 0,hashFunc = 0;

			for (int i = 0; i < fulName.length(); i++) {
				Sum = Sum + ((int) fulName.charAt(i));
			}
			//have to decrement the sum
			hdash = (Sum -2) % T.length;
			//hdash = Sum % HashTable.length;

			for (int j = 0; j < T.length; j++) {
				hashFunc = (hdash + (j * j)) % T.length;
				if (T[hashFunc] == -1) {
					T[hashFunc] = InsertingIndex;
					break;
				}
			}

		}
	
	//-----------------------------------------------------------------------------------------------------------------------------
		// This method is to search the record in arrays.
		public static void search(String toSearch) {

			int sum = 0;
			for (int i = 0; i < toSearch.length(); i++) {
				sum = sum + ((int) toSearch.charAt(i)); 
			}
			
			int hdash = 0;
			int hashFunc = 0;
			//System.out.println("sum is :" + sum);
			hdash = (sum-2) % T.length; // decreaseing sum -2 as per Prp

			int find = -2;
			for (int j = 0; j < T.length; j++) {
				int numCount = 0;
				hashFunc = (hdash + (j * j)) % T.length;
				if(hashFunc<0) {
					break;
				}
				if (T[hashFunc] != -1) {
					// matching each char toSearch with index of A 
					for (int k = 0; k < toSearch.length(); k++) {
						if (toSearch.charAt(k) == A[k + T[hashFunc]]) {
							numCount = numCount + 1; // for the legth of tosearch
						} else {
							break;
						}
					}
					if (numCount == toSearch.length()) {
						find = T[hashFunc];
						break;
					}
				}
			}
			//System.out.println("found is at  :" + find);
			//System.out.println("\nSearch Results ------------------");
			if (find == -2) {
				System.out.println(toSearch + " not found");
			} else {
				System.out.println(toSearch + " found at slot : " + hashFunc);
			}

		}	
	
		
		//-------------------------------------------------------------------------------------------------------
		// This function is to delete the records.
		public static void delete(String toDelete) {

			int sum = 0;
			int pointer = -2;
			for (int i = 0; i < toDelete.length(); i++) {
				sum = sum + ((int) toDelete.charAt(i));
			}
			int hdash = 0;
			hdash = (sum-2) % T.length;

			int hashFunc = 0;
			for (int j = 0; j < T.length; j++) {
				 int count = 0;
				hashFunc = (hdash + (j * j)) % T.length;
				if(hashFunc<0) {
					break;
				}
				if (T[hashFunc] != -1) {// matching each char toSearch with index of A 

					for (int k = 0; k < toDelete.length(); k++) {
						if (toDelete.charAt(k) == A[k + T[hashFunc]]) {
							count = count + 1;
						} else {
							break;
						}
					}

					if (count == toDelete.length()) {// if each word matches then both count will be same.
						pointer = T[hashFunc]; // setting the position of word
						break;
					}
				}

			}

			//System.out.println("\nDelete Result-------------------");
			if (pointer == -2) {
				System.out.println(toDelete + " not found,So can't be deleted");
			} else { //looping till end of the word
				while ((int) A[pointer] != 0) {
					A[pointer] = '*';
					pointer = pointer + 1;
				}
				T[hashFunc] = -1;
				System.out.println(toDelete + " deleted from slot : " + hashFunc);

			}
		}
		
		

		//-------------------------------------------------------------------------------------------------------
		// This method is to print the records.
		public static void printArray() {
			System.out.println("\nPrinting the Structures--------------------------------------");

			System.out.print("T:                                A : ");
			
			//printing A
			for (int i = 0; i < A.length; i++) {
				if ((int) A[i] == 32) {
					break;
				}
				if ((int) A[i] == 0) {
					System.out.print("/");
				} else {
					System.out.print(A[i]);
				}

			}

			System.out.println();

			//printing T
			for (int i = 0; i < T.length; i++) {
				if (T[i] != -1) {
					System.out.println(i + " : " + T[i]);
				} else {
					System.out.println(i + " : ");
				}

			}

			int totalSoltsOccup = 0;
			for (int i = 0; i < T.length; i++) {
				if (T[i] != -1) {
					totalSoltsOccup = totalSoltsOccup + 1;
				}
			}
			System.out.println("");
			//System.out.println("Total Slots Occupied : " + totalSoltsOccup);
			//System.out.println("Total Slots Unccupied : " + (T.length - totalSoltsOccup));
			int count = 0;
			for (int i = 0; i < A.length; i++) {
				count = count + 1;
				if ((int) A[i] == 32) {
					break;
				}
			}
		//	System.out.println("Total Slots in charTable : " + count);
			// System.out.println(Arrays.toString(CharTable));
		}
	

}
