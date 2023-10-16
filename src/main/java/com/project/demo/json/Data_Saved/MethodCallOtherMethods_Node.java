package com.project.demo.json.Data_Saved;

import java.util.ArrayList;
import java.util.List;

public class MethodCallOtherMethods_Node {
    private String methodName;                          // 目前 method 名稱
    private String method_className;                    // 目前 method 所在的 class 名稱
    private ArrayList<String> callMethods_str;          // 目前 method 所呼叫的所有 methods
    private ArrayList<MethodCallOtherMethods_Node> child_methods;        // 目前 method 所呼叫的所有 childNode -> 建樹用

    public MethodCallOtherMethods_Node(String methodName, String method_className, ArrayList<String> callMethods_str, ArrayList<MethodCallOtherMethods_Node> child_methods) {
        this.methodName = methodName;
        this.method_className = method_className;
        this.callMethods_str = callMethods_str;
        this.child_methods = child_methods;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setMethod_className(String method_className) {
        this.method_className = method_className;
    }

    public void setCallMethods_str(ArrayList<String> callMethods_str) {
        this.callMethods_str = callMethods_str;
    }

    public void setChild_methods(ArrayList<MethodCallOtherMethods_Node> child_methods) {
        this.child_methods = child_methods;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getMethod_className() {
        return method_className;
    }

    public ArrayList<String> getCallMethods_str() {
        return callMethods_str;
    }

    public ArrayList<MethodCallOtherMethods_Node> getChild_methods() {
        return child_methods;
    }

    public void addChild(MethodCallOtherMethods_Node node){
        child_methods.add(node);
    }

    // 尋找method所在的class
    public String findMethod_className(List<MethodCallOtherMethods_Node> node , String methodNameToFind){

        for (MethodCallOtherMethods_Node into_node : node){

            if(into_node.getMethodName().equals(methodNameToFind)){
                return into_node.getMethod_className();
            }
        }
        return "false";
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
