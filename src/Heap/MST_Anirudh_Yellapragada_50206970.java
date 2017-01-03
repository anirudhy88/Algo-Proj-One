package Heap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class MST_Anirudh_Yellapragada_50206970 {
    // Node Representation class
	public static class NodeRepresentation {
		int value; // number
		int key; // weight
		Boolean visited; // Bool to track if a node has already been visited
		NodeRepresentation parent; // In order to print the nodes included in
									// the MST
		// Constructor
		public NodeRepresentation(int value) {
			this.value = value;
			this.key = Integer.MAX_VALUE;
			this.visited = false;
			this.parent = null;
		}
	}

	// Heap class
	public static class MinHeap {
		int pos = 0;
		public int keys[];
		public NodeRepresentation array[]; // Integer array to store the key
											// values
											// of nodes in the heap
		public int position[]; // An integer variable to track the position of a
								// node in the heap
		public int size;

		// Constructor to initialize the above class variables
		public MinHeap(int size) {
			this.size = size + 1;
    		array = new NodeRepresentation[size + 1]; // array to store the
														// nodes
			position = new int[size + 1]; // array to track the positions of
											// each
											// node
		}

		// Method to insert an element into the heap
		public void insert(NodeRepresentation newNode) {
			array[++pos] = newNode;
			position[pos] = pos;
			// To maintain the heap property do heapify up
			heapifyUp(pos);
		}

		public void swap(NodeRepresentation array[], int posOne, int posTwo) {
			// swap the node
			NodeRepresentation tempNode = array[posOne];
			array[posOne] = array[posTwo];
			array[posTwo] = tempNode;

			// swap the position
			int temp = 0;
			temp = position[array[posOne].value];
			position[array[posOne].value] = position[array[posTwo].value];
			position[array[posTwo].value] = temp;
		}

		public void heapifyUp(int positionOfElement) {
			// To heapify up, compare the new element with it's parents till the
			// heap property is fulfilled
			int currentPosition = positionOfElement;

			while (currentPosition > 1 && array[currentPosition / 2].key > array[currentPosition].key) {
				swap(array, currentPosition / 2, currentPosition);
				currentPosition = currentPosition / 2;
			}
		}

		public NodeRepresentation extractMin() {
			if (size <= 1) {
				return null;
			}
			// Store the first node as it is has the minimum value
			NodeRepresentation minNode = array[1];

			// Replace the first node with the last one and then heapify down
	    	// Change the position
			position[array[size - 1].value] = 1;
			array[1] = array[size - 1];
		    size--;

			// Heapify down to maintain the heap property
			heapifyDown(1);
			return minNode;
		}

		public void heapifyDown(int elementPosition) {
			// Assuming the current element is the smallest
			int smallest = elementPosition;

			// Compare it with it's left and right nodes and heapify down to
			// maintain the property of the heap

			// Comparing with left node
			if (elementPosition * 2 < size && ((array[smallest].key) > (array[elementPosition * 2].key))) {
				smallest = elementPosition * 2;
			}

			// Comparing with right node
			if (elementPosition * 2 + 1 < size && array[smallest].key > array[elementPosition * 2 + 1].key) {
				smallest = elementPosition * 2 + 1;
			}

			// Determining the condition to swap
			if (smallest != elementPosition) {
				swap(array, elementPosition, smallest);
				heapifyDown(smallest);
			}
		}

		// Decrease key
		public void decreaseKey(NodeRepresentation node, int key) {
			int index = position[node.value];
			if (array[index].key < key)
				return;
			array[index].key = key;
			heapifyUp(index);
		}
	}

	// Graph class
	static class MyLinkedListOne extends LinkedList<NodeRepresentation> {

	}

	static class MyLinkedListTwo extends LinkedList<Integer> {

	}

	public static class Graph {
		int numOfVertices; // Variable to store the number of vertices in a
							// graph
		LinkedList<NodeRepresentation> adjacencyList[]; // Array of linked list
														// to store the edges
		LinkedList<Integer> weights[]; // array of linked lists to store weights

		// Constructor to initialize all the variables
		public Graph(int numVertices) {
			numOfVertices = numVertices;
			adjacencyList = new MyLinkedListOne[numOfVertices + 1];
			weights = new MyLinkedListTwo[numOfVertices + 1];

			for (int i = 0; i <= numOfVertices; i++) {
				adjacencyList[i] = new MyLinkedListOne();
				weights[i] = new MyLinkedListTwo();
			}

			for (int i = 0; i <= numOfVertices; i++) {
				NodeRepresentation node = new NodeRepresentation(i);
				adjacencyList[i].add(node);
			}
		}

		// Method to add an edge
		public void addAnEdge(int firstVertex, int secondVertex, int weight) {
			adjacencyList[firstVertex].add(adjacencyList[secondVertex].get(0));
			// As it is undirected we need to fill in both the ways
			adjacencyList[secondVertex].add(adjacencyList[firstVertex].get(0));
			weights[firstVertex].add(weight);
			weights[secondVertex].add(weight);
		}
	}

	public static int numOfEdges;
	public static int numOfVertices;

	public static void main(String[] args) throws IOException {
		int weightOfMST = 0;

		// Reading input file
		FileReader fr = new FileReader("D:\\531\\ProjectOne\\input.txt");
		BufferedReader br = new BufferedReader(fr);
		String currentLine = br.readLine();
		String[] tokenFirstLine = currentLine.split(" ");
		numOfVertices = Integer.parseInt(tokenFirstLine[0]);
		numOfEdges = Integer.parseInt(tokenFirstLine[1]);
		int edges = numOfEdges;
		MinHeap minHeap = new MinHeap(numOfVertices);

		// Create the graph base on input file
		Graph graph = new Graph(numOfVertices);

		while ((currentLine = br.readLine()) != null && edges != 0) {
			edges--;
			String[] token = currentLine.split(" ");
			graph.addAnEdge(Integer.parseInt(token[0]), Integer.parseInt(token[1]), Integer.parseInt(token[2]));
		}

		graph.adjacencyList[1].get(0).key = 0;
		graph.adjacencyList[1].get(0).parent = null;

		// Insert elements into minHeap
		for (int i = 1; i <= numOfVertices; i++) {
			minHeap.insert(graph.adjacencyList[i].getFirst());
		}

		while (minHeap.size > 1) {
			// As per prim's algorithm extract the MIN value
			// Take first vertex given in the input file to start with.
			NodeRepresentation min = minHeap.extractMin();

			// Calculating the weight of MST
			weightOfMST = weightOfMST + min.key;
			int i = 0;

			for (NodeRepresentation node : graph.adjacencyList[min.value]) {
				if (i == 0) {
					i++;
					continue;
				}
				// Finding the nearest node to include in the MST
				if (graph.weights[min.value].get(i - 1) < node.key && !node.visited) {
					node.key = graph.weights[min.value].get(i - 1);
					node.parent = min;
					minHeap.decreaseKey(node, node.key);
				}
				i++;
			}
			// Mark the vertex which has been visited
			min.visited = true;
		}

		// Creating output file
		File myFile = new File("D:\\531\\ProjectOne\\output.txt");
		if (!myFile.exists()) {
			myFile.createNewFile();
		}

		// Writing into output file
		FileWriter fw = new FileWriter(myFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(weightOfMST + "");
		System.out.println("Weight of MST Is " + weightOfMST);
		bw.newLine();
		int track = 0;
		for (LinkedList<NodeRepresentation> ll : graph.adjacencyList) {
			if (track != 0) {
				NodeRepresentation node = ll.getFirst();
				if (node.parent != null) {
					bw.write(node.parent.value + " " + node.value);
					bw.newLine();
				}
			}
			track++;
		}
		br.close();
		bw.close();
	}
}
