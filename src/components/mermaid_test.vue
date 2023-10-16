<template>
  <div class="mermaid-container">
    <h2 class="title-style">Method Relationship，click button can see Method Code</h2>
    <p class="now-style">Now is in： {{type}}  {{url}}</p>
    <div class="inside_container">
      <div id="myDiagramDiv" style=" width:100%; height:500px"></div>
    </div>
    <button class="back-button" @click="goToEndpoint()"> ◀ Back </button>
  </div>
  
</template>

<script>
import ast_data from "./AST_output.json";
import all_class from "./All_Class_Method.json";

import * as go from 'gojs';

export default {
  data() {
    return {
      type:"",
      url:"",
      messageToChild: "",
      diagram: null,
      nodedata : [],
      linkdata : [],
      now_key_num : -1,
      need_add: 0,
      no_link: 0,
    };
  },
  mounted() {
    this.initDiagram();
  },
  methods: {
    someButtonClickHandler(e, obj) {  //  when button been clicked
        
      var node = obj.part; // get button node
      var methodName = node.data.name; 
      var methodText = obj.panel.data.name;
      var type_save = this.type;
      var index_save = this.messageToChild;
      
      // console.log("Button clicked for node: " + methodName + "\n" + "Method name: " + methodText);
      // this.$router.push({ name: 'codedisplay', params: { methodName, methodText, type_save , index_save} });
      this.$router.push({ path: `/codedisplay/${methodName}/${methodText}/${type_save}/${index_save}`});

    },
    initDiagram() {
      this.messageToChild = this.$route.params.messageToChild;
      this.type = this.$route.params.type + "\t\t";
      var i = this.messageToChild;
      console.log("i = "+ i);

      // 確保有找到
      if (i >= 0) { 
        this.url = ast_data[i].endpoint;
        var get_list = all_class[ast_data[i].className];
        var method_list = [];

        for (var h=0; h < get_list.length ; h++){
          var choose_color = "lightblue";
          if (get_list[h] == ast_data[i].methodName){
            choose_color = "pink";
          }
          var add_into_method_list = {
            name: get_list[h], color: choose_color
          }
          method_list.push(add_into_method_list);
        }

        var add_class_node = {
          key: ++this.now_key_num,
          name: ast_data[i].className,
          nodecolor: "lightyellow",
          methods: method_list
        }
        this.nodedata.push(add_class_node);
        
        this.check_child(ast_data[i], this.now_key_num-1 );
      }
      else{
        console.log("can not find");
      }

      const $ = go.GraphObject.make;
      
      var diagram =
        new go.Diagram("myDiagramDiv", 
          {
            "undoManager.isEnabled": true,
            layout: $(go.TreeLayout,
              { // this only lays out in trees nodes connected by "generalization" links
                angle: 90,
                // path: go.TreeLayout.PathSource,  // links go from child to parent
                setsPortSpot: true,  // keep Spot.AllSides for link connection spot
                setsChildPortSpot: true,  // keep Spot.AllSides
                // nodes not connected by "generalization" links are laid out horizontally
                arrangement: go.TreeLayout.ArrangementHorizontal,
              })
          });
        
      // the item template for methods
      var methodTemplate =
        $(go.Panel, "Horizontal",
          $(go.Panel, "Button",  // 將按鈕嵌在 panel 中
            {
              click: this.someButtonClickHandler,  // clicked button function
              width: 35,  // button width
              height: 20,  // button height
              margin: 4,
            },
            $(go.Shape, "Rectangle", new go.Binding("fill", "color") ,{ stroke: null })   ,
            $(go.TextBlock, "click", {
              font: "10pt sans-serif",
              margin: 4, 
              alignment: go.Spot.TopRight
            }),             
          ),          
          
          $(go.TextBlock,
            { isMultiline: false, editable: true },
            new go.Binding("text", "name").makeTwoWay(),
          ),           
          // method parameters
          $(go.TextBlock, "()",
            // this does not permit adding/editing/removing of parameters via inplace edits
            new go.Binding("text", "parameters")
          ),          
  
        );
        
      diagram.nodeTemplate =
        $(go.Node, "Auto",
          {
            locationSpot: go.Spot.Center,
            fromSpot: go.Spot.AllSides,
            toSpot: go.Spot.AllSides
          },
          $(go.Shape, new go.Binding("fill", "nodecolor")),
          $(go.Panel, "Table",
            { defaultRowSeparatorStroke: "black" },
            // header
            $(go.TextBlock,
              {
                row: 0, columnSpan: 2, margin: 5, alignment: go.Spot.Center,
                font: "bold 12pt sans-serif",
                isMultiline: false, editable: false,

              },
              new go.Binding("text", "name").makeTwoWay()),

            // methods
            $(go.TextBlock, "Methods",
              { row: 2, font: "italic 10pt sans-serif"},
              new go.Binding("visible", "visible", v => !v).ofObject("METHODS")),
            $(go.Panel, "Vertical", { name: "METHODS" },
              new go.Binding("itemArray", "methods"),
              {
                row: 2, margin: 4, stretch: go.GraphObject.Fill,
                defaultAlignment: go.Spot.Left, background: "lightyellow",
                itemTemplate: methodTemplate
              }
            ),
            $("PanelExpanderButton", "METHODS",
              { row: 2, column: 1, alignment: go.Spot.TopRight, visible: false },
              new go.Binding("visible", "methods", arr => arr.length > 0),
            ),
            
          )
        );
      function linkStyle() {
        return { isTreeLink: false, fromEndSegmentLength: 0, toEndSegmentLength: 0 };
      }

      // 連接箭頭
      diagram.linkTemplate =  // by default, "Inheritance" or "Generalization"
        $(go.Link , linkStyle(), { isTreeLink: true, routing: go.Link.AvoidsNodes},
          $(go.Shape),
          $(go.Shape, { toArrow: "Triangle", fill: "white" })
        );
      // 不同的 class 關係：依賴、繼承等
      // diagram.linkTemplateMap.add("Association",
      //   $(go.Link, linkStyle(),
      //     $(go.Shape)
      //   ));

      // diagram.linkTemplateMap.add("Realization",
      //   $(go.Link, linkStyle(),
      //     $(go.Shape, { strokeDashArray: [3, 2] }),
      //     $(go.Shape, { toArrow: "Triangle", fill: "white" })
      //   ));

      // diagram.linkTemplateMap.add("Dependency",
      //   $(go.Link, linkStyle(),
      //     $(go.Shape, { strokeDashArray: [3, 2] }),
      //     $(go.Shape, { toArrow: "OpenTriangle" })
      //   ));

      // diagram.linkTemplateMap.add("Composition",
      //   $(go.Link, linkStyle(),
      //     $(go.Shape),
      //     $(go.Shape, { fromArrow: "StretchedDiamond", scale: 1.3 }),
      //     $(go.Shape, { toArrow: "OpenTriangle" })
      //   ));

      // diagram.linkTemplateMap.add("Aggregation",
      //   $(go.Link, linkStyle(),
      //     $(go.Shape),
      //     $(go.Shape, { fromArrow: "StretchedDiamond", fill: "white", scale: 1.3 }),
      //     $(go.Shape, { toArrow: "OpenTriangle" })
      //   ));

      // setup a few example class nodes and relationships
          


      diagram.model = new go.GraphLinksModel(
        {
          copiesArrays: true,
          copiesArrayObjects: true,
          linkCategoryProperty: "relationship",
          nodeDataArray: this.nodedata,
          linkDataArray: this.linkdata
        });
    },
    
    goToEndpoint() {
      this.$router.push({name:'endPoint'});
    },
    check_child(nowC,ccopy_j) {
      
      if(nowC.child_methods != null){
        for(var k = 0; k< nowC.child_methods.length ; k++){

          var now = nowC.child_methods[k];
          var get_list = all_class[now.method_className];
          var method_list = [];
          var save_q = -1;
          this.need_add = 1;// 不需要新建一個 node

          // 判斷相同一個 class 放入一個 node 即可，並改按鈕顏色
          for (var q = 0; q < this.nodedata.length; q++){
            if (now.method_className === this.nodedata[q].name) {
              
              for (var t = 0; t < this.nodedata[q].methods.length ; t++){
                if (now.methodName === this.nodedata[q].methods[t].name){
                  this.nodedata[q].methods[t].color = "pink";
                  
                }
              }
              this.need_add = 0;
              save_q = q;
            }
          }

          if(this.need_add === 1) {
            for (var h=0; h < get_list.length ; h++){
              var choose_color = "lightblue";
              if (get_list[h] == now.methodName){
                choose_color = "pink";
              }
  
              var add_into_method_list = {
                name: get_list[h], color: choose_color
              }
              method_list.push(add_into_method_list);
            }
            
            var add_class_node = {
              key: ++this.now_key_num,
              name: now.method_className,
              nodecolor: "lightyellow",
              methods: method_list
            }
            this.nodedata.push(add_class_node);
  
            var add_draw_line = { from: ccopy_j+1, to: this.now_key_num};
            this.linkdata.push(add_draw_line);
          }
          else {
            this.no_link = 0;
            for(var g = 0; g < this.linkdata.length; g++){
              if (save_q == this.linkdata[g].to && ccopy_j+1 == this.linkdata[g].from){
                this.no_link = 1;
              }

            }
            if (this.no_link == 0) {
              var add_draw_line = { from: ccopy_j+1, to: save_q};
              this.linkdata.push(add_draw_line);
            }
            
          }
          console.log(this.nodedata);
          this.check_child(now, this.now_key_num-1 );
        }
      }
    },
  },
};
</script>

<style>
.now-style{
  color: #90A4AE;
  font-weight: lighter;
  margin-left: 6%;
}
.mermaid {
  max-width: 100%;
}
.back-button{
  background-color: #78909C;
  color: #fff;
  border-radius: 15px;
  font-size: 100%;
  margin-left: 45%;
  margin-bottom: 2%;
}
.mermaid-container {
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 5%;
  background-color: #cfd8dc;
  margin-top: 50px;
  min-height: 200px;
  text-align: left;
  position: relative;
  padding: 20px;
  border-radius: 20px;
}
.title-style {
  color: #455a64;
  font-weight: bold;
  margin-top: 5%;
  margin-left: 6%;
}
.inside_container {
  text-align: left;
  max-width: 1200px;
  margin-top: 2%;
  margin-left: 5%;
  margin-right: 5%;
  margin-bottom: 5%;
  background-color: #eceff1;
  min-height: 200px;
  text-align: left;
  position: relative;
  padding: 20px;
  border-radius: 20px;
}
</style>
