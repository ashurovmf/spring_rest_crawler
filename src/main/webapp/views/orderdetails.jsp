<%@ page buffer="none" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="_csrf" content="${_csrf.token}"/>
<title>Order details</title>
</head>
<body>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/Dynatable/0.3.1/jquery.dynatable.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/Dynatable/0.3.1/jquery.dynatable.js"></script>
<script>
window.onload=function(){
    getOrderDetails(showOrder);
}
function getOrderDetails(callback){
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
      if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
        callback(xmlHttp.responseText);
    }
    xmlHttp.open("GET","/restcrawler/order/get?id=${order_id}", true); // false for synchronous request
    xmlHttp.setRequestHeader('Authorization','Bearer ' + window.sessionStorage.accessToken);
    xmlHttp.send();
}
function closeOrder(callback) {
   var categoryId = category.value.slice(4);
   var xmlHttp = new XMLHttpRequest();
   xmlHttp.onreadystatechange = function() {
     if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
        callback(xmlHttp.responseText);
   }
   xmlHttp.open("POST","/restcrawler/order/close?id=${order_id}", true); // false for synchronous request
   xmlHttp.setRequestHeader('Authorization','Bearer ' + window.sessionStorage.accessToken);
   xmlHttp.send();
}
function showOrder(order){
    console.log(order);
    var p = JSON.parse(order);
    renderResults(p[0].results)
}
function renderResults(results){
    console.log(results);
    $('#result-table').dynatable({
      dataset: {
        records: results
      }
    });
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
    <h1>Order '${order_id}'</h1>
</div>
<div style="border:1px solid black;">
    <h1>Searching result</h1>
    <div id="results" style="width:400px; height:700px">
    <table border="1" id="result-table" style="width:300px;">
      <thead>
        <th>Id</th>
        <th>Status</th>
        <th>ItemLink</th>
      </thead>
      <tbody>
      </tbody>
    </table>
    </div>
</div>
</body>
</html>