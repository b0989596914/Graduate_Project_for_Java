package com.project.demo.json.createAst;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.demo.json.Data_Saved.MethodCallOtherMethods_Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.project.demo.json.RunParser_search.VisitorFilesToGetData.*;

public class annotationAST {

    static MethodCallOtherMethods_Node to_find = new MethodCallOtherMethods_Node(null,null,null,null);
    static ArrayList<String> CallMethods = new ArrayList<>();

    static int space_count=11; //空格數，看起來有一層一層的
    static int method_index;
    static int child_index ; //用來記錄method、child所在的index
    static ArrayList<MethodCallOtherMethods_Node> list_for_child = new ArrayList<>(); //存child_method

    public static void find_annotation_AST() throws JsonProcessingException {

        for(int i=0;i<annotation_Node.size();i++){

            System.out.println(annotation_Node.get(i).getAnnotations()+"---"+annotation_Node.get(i).getMethodName()); //印接口、接口底下的method
            method_index = to_find.find_index(methodCall_Node,annotation_Node.get(i).getMethodName()); //找接口底下的method在MethodCallOtherMethods_Node裡的index
            //判斷接口底下的method所呼叫的method是否為使用者自定義
            CallMethods = methodCall_Node.get(method_index).getCallMethods_str();
            for(String temp_CallMethods : CallMethods) {
                for (int j = 0; j < 8; j++) {
                    System.out.print(" ");
                }

                // 輸出
                System.out.print("|");
                System.out.println("___" + temp_CallMethods + "     ");
                String[] arrSplit = temp_CallMethods.split(":");

                find_all_called(arrSplit[1],methodCall_Node); //繼續往下一層找

                // add child to child_methods
                child_index = to_find.find_index(methodCall_Node,arrSplit[1]);
                MethodCallOtherMethods_Node ChildNeedToAdd = methodCall_Node.get(child_index); // ChildNeedToAdd存子孫的node
                list_for_child.add(ChildNeedToAdd); //把Child_node加進list_for_child這個arraylist裡

            }
            annotation_Node.get(i).setChild_methods((ArrayList<MethodCallOtherMethods_Node>) list_for_child.clone());

            ASTBuild.add(annotation_Node.get(i));

            // 檢查存到的child_methods有沒有正確
//            System.out.print(ChildWillAddTo_annotation.getMethodName()+"-----"); //印現在的method name
//            for(MethodCallOtherMethods_Node check : annotation_Node.get(i).getChild_methods()){
//                System.out.print(check.getMethodName()+"、");
//            }
//            System.out.println();
            // 清空list，不然會存到之前的
            list_for_child.clear();
        }

    }

    public static String find_all_called(String method, List<MethodCallOtherMethods_Node> node_for_method) throws JsonProcessingException {
        Stack<String> stack = new Stack<>();
        Stack<MethodCallOtherMethods_Node> childStack = new Stack<>();
        Stack<Integer> spaceCountStack = new Stack<>();
        Stack<Boolean> addChildrenStack = new Stack<>();

        MethodCallOtherMethods_Node ChildWillAddTo;
        MethodCallOtherMethods_Node ChildNeedToAdd = null;
        String[] arrSplit = new String[1000];

        String result = "null";
        int methodIndex = -1;
        int methodIndexNow = -1;
        int childIndex = -1;
        int spaceCount = 11;
        boolean addChild = false;


        methodIndex = to_find.find_index(methodCall_Node, method); //找method所在的index
        if (methodIndex != -1) {
            CallMethods = node_for_method.get(methodIndex).getCallMethods_str();
        }

        // 如果 method 沒有呼叫別的 method 了就離開
        if (CallMethods == null || CallMethods.size() == 0) {
            return result;
        }

        stack.push(method);
        childStack.push(methodCall_Node.get(methodIndex));
        spaceCountStack.push(spaceCount);
        addChildrenStack.push(addChild);

        while (!stack.isEmpty()) {
            method = stack.pop();
            ChildWillAddTo = childStack.pop();
            spaceCount = spaceCountStack.pop();
            addChild = addChildrenStack.pop();

            if (addChild) {
                ChildWillAddTo.setChild_methods((ArrayList<MethodCallOtherMethods_Node>) list_for_child.clone());

                list_for_child.clear();
            }

            for (String temp_CallMethods : CallMethods) {
                methodIndexNow = to_find.find_index(methodCall_Node, temp_CallMethods); //找method所在的index
                for (int j = 0; j < spaceCount; j++) {
                    System.out.print(" ");
                }
                System.out.print("|");
                System.out.println("___" + temp_CallMethods + "     ");

                arrSplit = temp_CallMethods.split(":");

                spaceCount += 5; //控制層數

                if (methodIndexNow != -1 && node_for_method != null) {
                    stack.push(arrSplit[1]);
                    childStack.push(methodCall_Node.get(methodIndexNow));
                    spaceCountStack.push(spaceCount);
                    addChildrenStack.push(true);
                } else {
                    addChild = false;
                }

                spaceCount -= 5;
            }
        }
        return result;

    }
}