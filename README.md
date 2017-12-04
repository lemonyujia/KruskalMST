# KruskalMST
Using Priority Queue(minHeap) and Disjoint Sets data structure to achieve MST in Kruskal's Algorithm.

:point_right:Tips:
- Initialize a graph with N vertices and 0 edges, then implement Krushal's Algorithm to add needed edges. 
- We can use hashMap/Map or List/AraayList to store all edges. (I choosed the later)
- Disjoint Set data structure provide union/find that can avoid cycle inside graph happens.
- Class Edge must extends to Comparabel Interface.
- Priority Queue (minHeap) is more efficent to find the cheapest cost edge.
- Using List.get(int index) and List.indexOf(Object 0) to swap between index and value. (Vertices are strings in this input file)

If you have any comments or suggestions, please feel free to let me know.
