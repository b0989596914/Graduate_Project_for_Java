package com.project.demo.json.Data_Saved;

import java.util.ArrayList;
import java.util.List;

public class AnnotationCorrespondingMethod_Node {
    private String annotations;  // annotation 為 PostMapping、GetMapping 等等，包含網址
    private String methodName;       // annotation 對應的 method 名稱
    private String className;        // annotation 在的class
    private String endpoint;
    private ArrayList<MethodCallOtherMethods_Node> child_methods;

    public AnnotationCorrespondingMethod_Node(String annotations, String methodName, String className, String endpoint,ArrayList<MethodCallOtherMethods_Node> child_methods) {
        this.annotations = annotations;
        this.methodName = methodName;
        this.className = className;
        this.endpoint = endpoint;
        this.child_methods = child_methods;
    }

    public String getAnnotations() {
        return annotations;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getClassName() {
        return className;
    }

    public String getEndpoint() { return endpoint; }

    public ArrayList<MethodCallOtherMethods_Node> getChild_methods() {
        return child_methods;
    }

//    public void setChild_methods(ArrayList<MethodCallOtherMethods_Node> child_methods) {
//        this.child_methods = child_methods;
//    }

    public void setChild_methods(ArrayList<MethodCallOtherMethods_Node> child_methods) {
        this.child_methods = child_methods;
    }

    // 尋找method所在的index
    public int find_index(List<MethodCallOtherMethods_Node> node , String methodNameToFind){

        for(int i = 0 ; i < node.size(); i++){

            if(node.get(i).getMethodName().equals(methodNameToFind)){
                return i;
            }
        }
        return -1;
    }
}

