package chrisxue815.fv;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import chrisxue815.fv.models.Item;
import chrisxue815.fv.models.Prototype;
import chrisxue815.fv.models.Recipe;

public class GraphGenerator {

  public static Workspace create(Prototype prototype) {
    HashMap<String, Recipe> recipes = prototype.raw.recipes;
    HashMap<String, String> icons = prototype.raw.icons;
    
    // Init a project - and therefore a workspace
    ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
    pc.newProject();
    Workspace workspace = pc.getCurrentWorkspace();

    // Get a graph model - it exists because we have a workspace
    GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class)
        .getModel();
    GraphFactory factory = graphModel.factory();
    DirectedGraph directedGraph = graphModel.getDirectedGraph();
     
    //List node columns
    AttributeController ac = Lookup.getDefault().lookup(AttributeController.class);
    AttributeModel model = ac.getModel();

    //Add image column
    AttributeColumn imageCol = model.getNodeTable().addColumn("image", AttributeType.STRING);
    int imageIndex = imageCol.getIndex();
    
    for (Recipe recipe : recipes.values()) {
      for (Item result : recipe.results) {
        String name = result.name;
        Node node = factory.newNode(name);
        node.getNodeData().setLabel(name);
        node.getNodeData().setSize(10);
        node.getAttributes().setValue(imageIndex, icons.get(name));
        directedGraph.addNode(node);
      }
      for (Item ingredient : recipe.ingredients) {
        String name = ingredient.name;
        Node node = directedGraph.getNode(name);
        if (node == null) {
          node = factory.newNode(name);
          node.getNodeData().setLabel(name);
          node.getNodeData().setSize(10);
          node.getNodeData();
          node.getAttributes().setValue(imageIndex, icons.get(name));
          directedGraph.addNode(node);
        }
      }
    }
    
    for (Recipe recipe : recipes.values()) {
      for (Item result : recipe.results) {
        for (Item ingredient : recipe.ingredients) {
          Node ingredientNode = directedGraph.getNode(ingredient.name);
          Node resultNode = directedGraph.getNode(result.name);
          Edge edge = factory.newEdge(ingredientNode, resultNode, 1f, true);
          directedGraph.addEdge(edge);
        }
      }
    }

    // Count nodes and edges
    System.out.println("Nodes: " + directedGraph.getNodeCount() + " Edges: "
        + directedGraph.getEdgeCount());

    // Get a UndirectedGraph now and count edges
    UndirectedGraph undirectedGraph = graphModel.getUndirectedGraph();
    // The mutual edge is automatically merged
    System.out.println("Edges: " + undirectedGraph.getEdgeCount());

    // Iterate over nodes
    for (Node n : directedGraph.getNodes()) {
      Node[] neighbors = directedGraph.getNeighbors(n).toArray();
      System.out.println(n.getNodeData().getLabel() + " has "
          + neighbors.length + " neighbors");
    }

    // Iterate over edges
    for (Edge e : directedGraph.getEdges()) {
      System.out.println(e.getSource().getNodeData().getId() + " -> "
          + e.getTarget().getNodeData().getId());
    }

    // Modify the graph while reading
    // Due to locking, you need to use toArray() on Iterable to be able to
    // modify
    // the graph in a read loop
    for (Node n : directedGraph.getNodes().toArray()) {
      //directedGraph.removeNode(n);
    }

    return workspace;
  }

  public static void export(Workspace workspace) {
    // Export full graph
    ExportController ec = Lookup.getDefault().lookup(ExportController.class);
    try {
      ec.exportFile(new File("bin/prototypes.gexf"));
    } catch (IOException ex) {
      ex.printStackTrace();
      return;
    }
  }

  public static void exec(Prototype prototype) {
    GraphGenerator.export(GraphGenerator.create(prototype));
  }

}
