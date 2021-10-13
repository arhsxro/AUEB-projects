

function changeColor() {
  document.getElementById("demo").style.color = "red";
}

function changeColor1() {
  document.getElementById("demo").style.color = "black";
}

function changeColor2() {
  document.getElementById("demo").style.color = "green";
}

function changeColor3() {
  document.getElementById("demo").style.color = "magenta";
}

function changeColor4() {
  document.getElementById("demo").style.color = "white";
}

function changeColor5() {
  document.getElementById("demo").style.color = "blue";
}

function changeColor6() {
  document.getElementById("demo").style.color = "brown";
}

function changeColor7() {
  document.getElementById("demo").style.color = "yellow";
}

function changeColor8() {
  document.getElementById("demo").style.color = "orange";
}

function changeColor9() {
	var x =document.getElementById("demo").querySelectorAll("li");
	for (i = 0; i < x.length; i++) {
		x[i].style.color = "red";
	}
}
function changeColor10() {
	var x =document.getElementById("demo").querySelectorAll("li");
	for (i = 0; i < x.length; i++) {
		x[i].style.color = "black";
	}
}
function changeColor11() {
	var x =document.getElementById("demo").querySelectorAll("li");
	for (i = 0; i < x.length; i++) {
		x[i].style.color = "green";
	}
}
function changeColor12() {
	var x =document.getElementById("demo").querySelectorAll("li");
	for (i = 0; i < x.length; i++) {
		x[i].style.color = "magenta";
	}
}
function changeColor13() {
	var x =document.getElementById("demo").querySelectorAll("li");
	for (i = 0; i < x.length; i++) {
		x[i].style.color = "white";
	}
}
function changeColor14() {
	var x =document.getElementById("demo").querySelectorAll("li");
	for (i = 0; i < x.length; i++) {
		x[i].style.color = "blue";
	}
}
function changeColor15() {
	var x =document.getElementById("demo").querySelectorAll("li");
	for (i = 0; i < x.length; i++) {
		x[i].style.color = "brown";
	}
}
function changeColor16() {
	var x =document.getElementById("demo").querySelectorAll("li");
	for (i = 0; i < x.length; i++) {
		x[i].style.color = "yellow";
	}
}
function changeColor17() {
	var x =document.getElementById("demo").querySelectorAll("li");
	for (i = 0; i < x.length; i++) {
		x[i].style.color = "orange";
	}
}

function changeColor18() {
	var x =document.getElementById("demo").querySelectorAll("p");
	for (i = 0; i < x.length; i++) {
		x[i].style.color = "red";
	}
}
function changeColor19() {
	var x =document.getElementById("demo").querySelectorAll("p");
	for (i = 0; i < x.length; i++) {
		x[i].style.color = "black";
	}
}
function changeColor20() {
	var x =document.getElementById("demo").querySelectorAll("p");
	for (i = 0; i < x.length; i++) {
		x[i].style.color = "green";
	}
}
function changeColor21() {
	var x =document.getElementById("demo").querySelectorAll("p");
	for (i = 0; i < x.length; i++) {
		x[i].style.color = "magenta";
	}
}
function changeColor22() {
	var x =document.getElementById("demo").querySelectorAll("p");
	for (i = 0; i < x.length; i++) {
		x[i].style.color = "white";
	}
}
function changeColor23() {
	var x =document.getElementById("demo").querySelectorAll("p");
	for (i = 0; i < x.length; i++) {
		x[i].style.color = "blue";
	}
}
function changeColor24() {
	var x =document.getElementById("demo").querySelectorAll("p");
	for (i = 0; i < x.length; i++) {
		x[i].style.color = "brown";
	}
}
function changeColor25() {
	var x =document.getElementById("demo").querySelectorAll("p");
	for (i = 0; i < x.length; i++) {
		x[i].style.color = "yellow";
	}
}
function changeColor26() {
	var x =document.getElementById("demo").querySelectorAll("p");
	for (i = 0; i < x.length; i++) {
		x[i].style.color = "orange";
	}
}

function changeDate() {
	var d = new Date();
	var n = d.getDay();
	
		if(n==0){
			document.getElementById("date").style.backgroundColor = "purple";
		} else if(n == 1){
			document.getElementById("date").style.backgroundColor = "green";
		} else if(n == 2){
			document.getElementById("date").style.backgroundColor = "brown";
		} else if(n == 3){
			document.getElementById("date").style.backgroundColor = "orange";
		} else if(n == 4){
			document.getElementById("date").style.backgroundColor = "blue";
		} else if(n == 5){
			document.getElementById("date").style.backgroundColor = "red";
		} else if(n == 6){
			document.getElementById("date").style.backgroundColor = "black";
		}
			
	
	
}

function changeDate1() {
	var d = new Date();
	var n = d.getDay();
	console.log(n);
		if(n==0){
			document.body.style.backgroundColor = "black";
		} else if(n == 1){
			document.body.style.backgroundColor = "brown";
		} else if(n == 2){
			document.body.style.backgroundColor = "red";
		} else if(n == 3){
			document.body.style.backgroundColor = "purple";
		} else if(n == 4){
			document.body.style.backgroundColor = "yellow";
		} else if(n == 5){
			document.body.style.backgroundColor = "orange";
		} else if(n == 6){
			document.body.style.backgroundColor = "green";
		}
			
	
	
}

function Val_Function() {
  var val = document.getElementById("old");
  if (!val.checkValidity()) {
	 val.setCustomValidity("your age must be >1 and <100");
  }
     

}

function Val1_Function(){
	var psw = document.getElementById("password");
	var confirmPassword = document.getElementById("confirmPassword");
	if(psw != confirmPassword){
	   confirmPassword.setCustomValidity("Passwords do not match.");
	   
	}
	
}

function Val2_Function(){
	var num = document.getElementById("phone");
	var temp = num.toString();
	var count = 0;
	if((temp.replace(/[^0-9]/g,"").length) != 10){
		num.setCustomValidity("Phone must have 10 digits");
	}		
	
}