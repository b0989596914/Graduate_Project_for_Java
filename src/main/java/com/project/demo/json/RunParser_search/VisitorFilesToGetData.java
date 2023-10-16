package com.project.demo.json.RunParser_search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;
import com.project.demo.DemoApplication;
import com.project.demo.json.Data_Saved.AnnotationCorrespondingMethod_Node;
import com.project.demo.json.Data_Saved.MethodCallOtherMethods_Node;
import com.project.demo.json.createAst.annotationAST;
import com.project.demo.service.FileStorageService;
import com.project.demo.splitClassAndSave;
import com.project.demo.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class VisitorFilesToGetData {

    public static int temp_num = 0;
    public static String endPointPrefix = "";
    public static String filename, innerClass_Method,temp_class;
    public static int space = 11; //空格數，看起來有一層一層的

    public static HashMap<Object, ArrayList<Object>> classHaveMethod_hashmap = new HashMap<>();

    public static ArrayList<AnnotationCorrespondingMethod_Node> ASTBuild = new ArrayList<>();

    public static List<MethodCallOtherMethods_Node> methodCall_Node = new ArrayList<>();
    public static List<AnnotationCorrespondingMethod_Node> annotation_Node = new ArrayList<>();

    public static ArrayList<String> temp = new ArrayList<>();
    public static ArrayList<String> use_methods = new ArrayList<>();                // method 呼叫的 method
    public static ArrayList<String> use_methods_class = new ArrayList<>();          // 被呼叫的 method 所在的 class
    public static ArrayList<String> method_create_by_user = new ArrayList<>();      // 自定義的 method
    public static ArrayList<String> use_methods_classAndMethod = new ArrayList<>(); // 存成 class: method

    public static String showCode_path = "";

    @Autowired
    private static FileStorageService fileStorageService;

    public static void initData(String path){
        temp_num = 0;
        filename = innerClass_Method = temp_class = endPointPrefix = "";
        space = 11;
        classHaveMethod_hashmap.clear();
        ASTBuild.clear();
        methodCall_Node.clear();
        annotation_Node.clear();
        temp.clear();
        use_methods.clear();
        use_methods_class.clear();
        method_create_by_user.clear();
        use_methods_classAndMethod.clear();
        showCode_path = path;
    }

    public static void Collating_Data() {       // 去除非自訂義的資料

        // 整理 methodCall_Node 裡面的 CallMethods_str 資料
        for (MethodCallOtherMethods_Node node : methodCall_Node) {

            ArrayList<String> save_new_method_list = new ArrayList<>();
            if (node.getCallMethods_str().size()!=0) {
                for (String get_method_in_str : node.getCallMethods_str()) {
                    MethodCallOtherMethods_Node find = new MethodCallOtherMethods_Node(null,null, null, null);

                    temp_class = find.findMethod_className(methodCall_Node, get_method_in_str);
                    if (temp_class.equals("false") == false) {      // 有找到東西
                        save_new_method_list.add(temp_class + ":" + get_method_in_str);         // 更新後，只存自訂義的被呼叫 method
                    }
                }
            }
            node.setCallMethods_str(save_new_method_list);
        }

        // 整理 use_methods 資料
        for (String use_method : use_methods) {
            MethodCallOtherMethods_Node find = new MethodCallOtherMethods_Node(null, null, null, null);

            // 判斷是否為使用者自定義的 method
            if (!find.findMethod_className(methodCall_Node, use_method).equals("false")) {
                use_methods_class.add(find.findMethod_className(methodCall_Node, use_method)); // 找到的class存入use_methods_class
                method_create_by_user.add(use_method);
//                use_methods_classAndMethod.add(use_methods_class.get(temp_num) + ":" + use_method); // 字串相加 -> class:method
            }
        }
        use_methods = (ArrayList<String>) use_methods_classAndMethod.clone();
    }

    public static String Data_Printer(String writePath) throws IOException {
/*
        System.out.println("\n----------------------Start: Print Hashmap for Class 下宣告過的 Method-------------------------------------");
        for (Object key: classHaveMethod_hashmap.keySet()){
            System.out.println(key+ " = " + classHaveMethod_hashmap.get(key));
        }
//        return_data.put("classHaveMethod",classHaveMethod_hashmap);
        System.out.println("---------------------End:  Print Hashmap for Class 下宣告過的 Method-------------------------------------\n");

        System.out.println("----------------------Start: Print methodCall_Node for all method-------------------------------------");
//        return_data.put("methodCallNode",methodCall_Node);
        for (MethodCallOtherMethods_Node node : methodCall_Node) {

            System.out.printf("%s (%s)\t", node.getMethodName(),node.getMethod_className() );

            // 印class:method
            System.out.print(node.getCallMethods_str());

            System.out.printf("\t%s\n",node.getChild_methods());
        }
        System.out.println("---------------------End: Print methodCall_Node for all method-------------------------------------\n");


        System.out.println("----------------------Start: Print annotation 對應的 class、method-------------------------------------");
//        return_data.put("annotationNode",annotation_Node);

        for (AnnotationCorrespondingMethod_Node node : annotation_Node) {
            System.out.println(node.getClassName() + "\t" + node.getMethodName() + "\t" + node.getAnnotations() + "\t" + node.getEndpoint());
        }
        System.out.println("---------------------End: Print annotation 對應的 class、method-------------------------------------\n");

        System.out.println("-------------Start: Print 有被呼叫過的所有 method (被呼叫的 method 之 class : 被呼叫的 method )------------------");
        System.out.println("有被呼叫過的所有 method (class: method) ");
//        return_data.put("usedMethod",use_methods);

        for (String use_method : use_methods) {
            System.out.println(use_method);
        }
        System.out.println("-------------End: Print 有被呼叫過的所有 method (被呼叫的 method 之 class : 被呼叫的 method )------------------\n");
*/

        System.out.println("---------------------Start: Print AST-------------------------------------");
        annotationAST test = new annotationAST();
        test.find_annotation_AST();
        System.out.println("---------------------End: Print AST-------------------------------------\n");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(ASTBuild);

        try {
            FileWriter fw = new FileWriter(writePath + "/AST_output.json");

            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(json);    // 寫入資料
            bw.flush(); // 把記憶體資料寫進去
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    // 尋找所有 java 檔案並掃描 -----------------------------------------------------------------
    public static void GoInFolder(File input_file) {
        Stack<File> stack = new Stack<>();
        stack.push(input_file);


        while (!stack.isEmpty()) {
            File current_file = stack.pop();
            File[] fs = current_file.listFiles();

            for (File f : fs) {
                if (f.isDirectory()) {
                    stack.push(f);
                }
                if (f.isFile() && f.getName().contains(".java")) {
                    String file_data_str = f.toString().replace("\\", "/");
                    String path = file_data_str.substring(0, file_data_str.lastIndexOf("/"));
                    filename = file_data_str.substring(file_data_str.lastIndexOf("/") + 1);

                    SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(DemoApplication.class).resolve(path));
                    CompilationUnit cu = sourceRoot.parse("", filename);

                    endPointPrefix = "";
                    classHaveMethod_hashmap.put(filename.substring(0, filename.length() - 5), new ArrayList<>());

                    cu.accept(new Method_Visitor(), null);
                }
            }
        }
    }

    private static class Method_Visitor extends VoidVisitorAdapter<Void> {
        public SimpleName NowClassName;

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg){     // 尋找 Class 外面的 Mapping 入口

            NowClassName = n.getName();

            for (AnnotationExpr class_annotation: n.getAnnotations()){
                String class_anno_Name = class_annotation.getName().toString();

                if (class_anno_Name.endsWith("Mapping") && class_annotation instanceof SingleMemberAnnotationExpr) {
                    String class_mem = ((SingleMemberAnnotationExpr) class_annotation).getMemberValue().toString();
                    if (class_mem != null && class_mem.length() > 3) {
                        endPointPrefix = class_mem.substring(1, class_mem.length() - 1);
                    }
                }
            }
            super.visit(n,arg);
        }

        @Override
        public void visit(MethodDeclaration n, Void arg) {
//            System.out.println("showCode_path: " + showCode_path);
            splitClassAndSave.buildShownCode(showCode_path, NowClassName, n.getName(),n);

            innerClass_Method = n.getName().toString();     // 可以用這個建立 class 與其包含的 method

            classHaveMethod_hashmap.get(filename.substring(0,filename.length()-5)).add(innerClass_Method); // 儲存 class 宣告過的各種 method name，如有重複也沒關係(hashmap會覆蓋掉)

            List<AnnotationExpr> annotationList = n.getAnnotations();  // 得到這個 method 的 annotation

            for (AnnotationExpr annotation : annotationList){
                String annotation_Name = annotation.getName().toString();
                if (annotation_Name.endsWith("Mapping") && !annotation_Name.startsWith("Request")) {     // 找到 PostMapping、GetMapping...等 Annotation
                    String httpMethod = annotation_Name.substring(0, annotation_Name.indexOf("Mapping"));   // 將每個接口名稱的 HTTP method 切割出來
                    String endPoint = endPointPrefix;

                    /*
                     *  NormalAnnotationExpr -> 該 annotation 有 0 or 多個 key-value pairs, ex. @Mapping(a=5, d=10)
                     *  SingleMemberAnnotationExpr -> 該 annotation 只有 1 個 value, ex. @Count(15)、@PostMapping("/uploads_File")
                     *  MarkerAnnotationExpr -> 該 annotation 只有 annotation type name, ex. @Override
                     */

                    if (annotation instanceof SingleMemberAnnotationExpr) {
                        String value = ((SingleMemberAnnotationExpr) annotation).getMemberValue().toString();

                        if (value != null && value.length() > 2) {
                            endPoint = endPointPrefix + value.substring(1, value.length() - 1);
                        }
                    }
                    else if (annotation instanceof NormalAnnotationExpr ) {
                        NormalAnnotationExpr normalAnnotation = (NormalAnnotationExpr) annotation;

                        // 遍历注解的成员键值对
                        for (MemberValuePair pair : normalAnnotation.getPairs()) {
                            String key = pair.getNameAsString();
                            if ("value".equals(key)) {
                                Expression valueExpr = pair.getValue();
                                if (valueExpr instanceof StringLiteralExpr) {
                                    String value = ((StringLiteralExpr) valueExpr).getValue();
                                    endPoint = value;
                                }
                            }
                        }
                    }
                    AnnotationCorrespondingMethod_Node mappingNode = new AnnotationCorrespondingMethod_Node(httpMethod, innerClass_Method, filename.substring(0,filename.length()-5), endPoint,null);
                    annotation_Node.add(mappingNode);
                }
            }
            super.visit(n,arg);
            MethodCallOtherMethods_Node CallNode = new MethodCallOtherMethods_Node(innerClass_Method, filename.substring(0, filename.length()-5), (ArrayList<String>) temp.clone(),null);
            methodCall_Node.add(CallNode);
            temp.clear();
        }

        @Override
        public void visit(MethodCallExpr n, Void arg) {
            temp.add(n.getNameAsExpression().toString());
            use_methods.add(n.getNameAsExpression().toString());      // 獲得 method name
        }
    }
}
