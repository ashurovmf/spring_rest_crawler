<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="_csrf" content="${_csrf.token}"/>
<title>User page</title>
</head>
<body>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/Dynatable/0.3.1/jquery.dynatable.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/Dynatable/0.3.1/jquery.dynatable.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
<script>
$(document).ready(function() {
  $(".js-example-basic-single").select2();
});
window.onload=function(){
    getUserOrders(showOrders);
    getCategories(renderCategories);
}
function getUserOrders(callback){
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
      if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
        callback(xmlHttp.responseText);
    }
    xmlHttp.open("GET","/restcrawler/order/me", true); // false for synchronous request
    xmlHttp.setRequestHeader('X-CSRF-TOKEN', '${_csrf.token}');
    xmlHttp.setRequestHeader('Authorization','Bearer ' + window.sessionStorage.accessToken);
    xmlHttp.send();
}
function getCategories(callback){
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
      if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
        callback(xmlHttp.responseText);
    }
    xmlHttp.open("GET","/restcrawler/category/get", true); // false for synchronous request
    xmlHttp.setRequestHeader('Authorization','Bearer ' + window.sessionStorage.accessToken);
    xmlHttp.send();
}
function createNewOrder(callback) {
   var name = document.getElementById('ordername').value;
   var keys = document.getElementById('key').value;
   var category = document.getElementById('category-select');
   var categoryId = category.value.slice(4);
   var xmlHttp = new XMLHttpRequest();
   xmlHttp.onreadystatechange = function() {
     if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
        callback(xmlHttp.responseText);
   }
   xmlHttp.open("POST","/restcrawler/order/change?name="+name+"&keys="+keys+"&categoryId="+categoryId, true); // false for synchronous request
   xmlHttp.setRequestHeader('Authorization','Bearer ' + window.sessionStorage.accessToken);
   xmlHttp.send();
}
function showOrders(orders){
    console.log(orders);
    var p = JSON.parse(orders);
    for(var i in p)
    {
      p[i].link = '<a href="/restcrawler/details?order='+p[i].id+'">Details</a>'
    }
    $('#order-table').dynatable({
      dataset: {
        records: p
      }
    });
}
function renderCategories(categories){
    var p = JSON.parse(categories);
    var rootSelect = document.getElementById('category-select');
    for(var i in p)
    {
         var id = p[i].id;
         var name = p[i].name;
         var level = p[i].level;
         var parent = p[i].parent;
         if ( level === "1" ){
             var optgroup = document.createElement('optgroup');
             optgroup.id = 'cat_'+id;
             optgroup.label = name;
             rootSelect.appendChild(optgroup);
         } else {
             var parentGroup = document.getElementById('cat_'+parent);
             var option = document.createElement('option');
             option.value = 'cat_'+id;
             option.appendChild(document.createTextNode(name));
             parentGroup.appendChild(option);
         }
    }
}
function orderCreated(order){
    console.log(order);
    var p = JSON.parse(order);
    var rootSelect = document.getElementById('crt-result');
    rootSelect.appendChild(document.createTextNode('Order '+p.name+'('+p.id+') created with status '+p.status));
    getUserOrders(showOrders);
}
</script>
<div style="border:1px solid black;">
    <h1>Current orders</h1>
    <div id="orders" style="width:400px; height:300px">
    <table border="1" id="order-table" style="width:300px; height:300px">
      <thead>
        <th>Id</th>
        <th>Name</th>
        <th>Status</th>
        <th>Link</th>
      </thead>
      <tbody>
      </tbody>
    </table>
    </div>
</div>
<div style="border:1px solid black;">
    <h1>Create new order</h1>
    <div id="neworder">
    Order name:<br>
    <input type="text" id="ordername"><br>
    Search sample:<br>
    <input type="text" id="key"><br>
    Category name:<br>
    <select id="category-select" class="js-example-basic-single" style="width:300px">
    </select>
    </div>
    <button id="crtNewOrder" onclick="createNewOrder(orderCreated);">Create</button><br>
    <span id="crt-result"></span>
</div>
</body>
</html>