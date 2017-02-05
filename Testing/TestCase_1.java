package com.cs542.project.testcase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import com.cs542.project.graph.DijkstraShortestPathAlgorithm;
import com.cs542.project.graph.Edge;
import com.cs542.project.graph.Graph;
import com.cs542.project.graph.Vertex;

/*This is the test case class which is used to run program on console.*/
public class TestCase_1 {
	private static ArrayList<Vertex> nodes;
	private static ArrayList<Edge> edges;
	private static Graph graph;
	private static int ID = 0;
	private static int LINK_ID = 0;
	private static Scanner scan;
	private static DijkstraShortestPathAlgorithm dij;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		nodes = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();

		for (int i = 0; i < 11; i++) {
			Vertex router = new Vertex("" + ID++, true);
			nodes.add(router);
		}

		graph = new Graph(nodes, edges);

		executePlan();

		// Modify Topology
		scan = new Scanner(System.in);
		String sId = scan.nextLine().toString();
		String dId = scan.nextLine().toString();

		int weight = Integer.parseInt(scan.nextLine());

		for (Edge e : edges) {
			if (e.getSource().getvertexId().equals(sId) && e.getDestination().getvertexId().equals(dId)) {
				e.setWeight(weight);
				break;
			}
		}
		executePlan();

		// Router Down
		String downId = scan.nextLine();

		nodes.get(Integer.parseInt(downId)).setUp(false);
		executePlan();

		System.out.println("\n Are you sure to Add new Router?[y/n]");
		String ans = scan.nextLine();
		if (ans.equals("y")) {
			Vertex v = new Vertex("" + ID++, true);
			nodes.add(v);
			addLink();
		}
		executePlan();

		System.out.println("BROADCAST:");
		// dij.broadCastRouter();
	}

	private static void addLink() {
		// TODO Auto-generated method stub
		boolean flag = true;
		while (flag) {
			System.out.println("Add Links");
			System.out.print("Source ID:");
			int sourceID = Integer.parseInt(scan.nextLine());
			System.out.print("Destination ID:");
			int desID = Integer.parseInt(scan.nextLine());
			System.out.print("Weight:");
			int w = Integer.parseInt(scan.nextLine());
			addLane("" + LINK_ID++, sourceID, desID, w);

			System.out.println("Link is Added.");

			System.out.println("Are you still want to add new Link?[y/n]");
			String ans = scan.nextLine();
			if (ans.contains("n")) {
				flag = false;
			}
		}
	}

	private static void executePlan() {
		// TODO Auto-generated method stub

		createTopology();
		dij = new DijkstraShortestPathAlgorithm(graph);
		dij.findPath(nodes.get(9));

		ArrayList<Vertex> path = dij.makeDijkstraPathFromSourceTo(nodes.get(0));
		int count = path.size();
		for (Vertex vertex : path) {
			if (count > 1)
				System.out.print(vertex.getvertexId() + "->");
			else
				System.out.print(vertex.getvertexId());
			count--;
		}
		System.out.println();
		System.out.println("Cost to reach from Host router to Destination Router:" + dij.costToReach(nodes.get(10)));

		// Routing Table
		HashMap<Vertex, Vertex> routingTable = dij.fetchRoutingTable();
		System.out.println("Routing Table For Router:" + nodes.get(0).getvertexId());
		System.out.println("--------------------------------------------------------");
		System.out.println("I/P \t O/P");
		System.out.println("------------");

		Iterator it = routingTable.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Vertex, Vertex> pair = (Entry) it.next();
			System.out.println(" " + pair.getKey().getvertexId() + "\t " + pair.getValue().getvertexId());
		}

	}

	private static void createTopology() {
		// TODO Auto-generated method stub

		addLane("" + LINK_ID++, 0, 1, 85);
		addLane("" + LINK_ID++, 0, 2, 217);
		addLane("" + LINK_ID++, 0, 4, 173);
		addLane("" + LINK_ID++, 2, 6, 186);
		addLane("" + LINK_ID++, 2, 7, 103);
		addLane("" + LINK_ID++, 3, 7, 183);
		addLane("" + LINK_ID++, 5, 8, 250);
		addLane("" + LINK_ID++, 8, 9, 84);
		addLane("" + LINK_ID++, 7, 9, 167);
		addLane("" + LINK_ID++, 4, 9, 502);
		addLane("" + LINK_ID++, 9, 10, 40);
		addLane("" + LINK_ID++, 1, 10, 20);
	}

	private static void addLane(String laneId, int sourceLocNo, int destLocNo, int duration) {
		Edge lane = new Edge(laneId, nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
		Edge lane1 = new Edge("" + LINK_ID++, nodes.get(destLocNo), nodes.get(sourceLocNo), duration);
		edges.add(lane);
		edges.add(lane1);
	}
}
