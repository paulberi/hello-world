package com.composite.Demo;

import java.util.ArrayList;

abstract class Node {
    private ArrayList<Node> children = new ArrayList<>();
    private String name;

    public Node(String name) {
        this.name = name;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public void addChild(Node node) {
        this.children.add(node);
    }

    abstract double getTemperature();

    // Optional on recursive navigation of trees
    private Node getSensorRecursive(Node node, String name) {
        if(node.getName().equals(name)) {
            return node;
        } else {
            for(Node n : node.getChildren()) {
                Node result = getSensorRecursive(n, name);
                if(result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public Node getSensorByName(String name) {
        Node node = getSensorRecursive(this, name);
        return node;
    }


}
