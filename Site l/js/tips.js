//$('#fixedImg2').on({

var pic = ['bk.jpg','bk2.jpg', 'bk3.png','bk4.png'];


function mvNextScr()
{
 nextP();
 nextT();
}
function mvPrevScr()
{
 prevP();
 prevT();
}

var i = 0;
function nextP(){
	if (i<pic.length-1){
		i++;
		$("#fixedImg2").attr('src',pic[i]);
	}
console.log(i);
}

function prevP(){
	if(i>0){
	i--;
	$("#fixedImg2").attr('src',pic[i]);
}
}



var t1 = "Step 1";
var t2 = "Step 2";
var t3 = "Step 3";
var t4 = "Step 4";

var txt = [t1,t2,t3,t4];
var l = 0;


function nextT(){
	if (l<txt.length-1){
		l++;
		$("#tekst").text(txt[l]);
	}
	console.log(l);
}

function prevT(){
	if(l>0){
		l--;
		$("#tekst").text(txt[l]);
	}
}