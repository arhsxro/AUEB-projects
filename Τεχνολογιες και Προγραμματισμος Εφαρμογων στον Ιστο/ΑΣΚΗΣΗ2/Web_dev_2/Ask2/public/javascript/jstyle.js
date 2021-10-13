



var MYLIST;
var mylist = {};
var templist = [];
var obj_de = [];
var obj_point = [];
var i;
var instead_of_i;
var point;
var global_point;
function findbook() {
    var el = document.getElementById('search').value;

    let url = 'https://reststop.randomhouse.com/resources/works?search=' + el;
    let init = {
        method: 'GET',
        headers: {
            Accept: 'application/json',
        }
    }

    fetch(url, init)
        .then(function (response) {
            return response.json();
        })
        .then(function (obj) {




            for (i = 0; i < obj.work.length; i++) {
                var html = '<div class="book' + i + '">' + '<p>' + obj.work[i].authorweb + '<p>' + obj.work[i].titleweb + '<p>' + obj.work[i].workid + '<button onclick="myList(' + i + ')" class="idd" id="' + i + '" type="submit" >Move book to Mylist</button>' + '</div>';
                document.querySelector('#books').insertAdjacentHTML('beforeend', html);

                templist.push(obj.work[i].authorweb, obj.work[i].titleweb, obj.work[i].workid);




            }




        }).catch(function (error) {
            console.error("Something went shit");
            console.error(error);
        });
}

function myList(i) {


    var z = i * 3;
    new_list = { author: templist[z], title: templist[z + 1], bookid: templist[z + 2] };

    let init = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(new_list)

    }
    fetch('/FindBook', init)
        .then(function (response) {
            return response.json();
        })
        .then(response => {

            MYLIST = response


            functionFav(i);
        });

}

function functionFav(i) {

    //localStorage.clear();
    console.log(localStorage.length);

    let obj = JSON.stringify(MYLIST);
    console.log(MYLIST.title);
    if (localStorage.getItem('data' + i) != null){
        if(JSON.parse(localStorage.getItem('data' + i)).title == MYLIST.title){
            alert("This book is already in your list");
        } else {
            let random = Math.floor(Math.random() * 200) + 50;
            alert(random)
            localStorage.setItem('data' + (random+i), obj);
        }
    } else {
        localStorage.setItem('data' +i,obj);
    }


    instead_of_i = i;

    


}

function displayList() {


    console.log(localStorage.length);

    for (i = 0; i < 300; i++) {
        var temp_data = -1;
        if (localStorage.getItem('data' + i) != null) {
            temp_data = i;
            
        }
        if (temp_data != -1) {
            
            obj_de.push(JSON.parse(localStorage.getItem('data' + temp_data)).author);
            obj_de.push(JSON.parse(localStorage.getItem('data' + temp_data)).title);
            obj_de.push(JSON.parse(localStorage.getItem('data' + temp_data)).bookid);
            obj_point.push(temp_data);
        }
    }



    var k = 0;
    for (i = 0; i < obj_de.length; i += 3) {
        point = obj_point[k];
        
        k++;

        var html1 = '<div class="book' + i + '">' + '<p>' + JSON.stringify(obj_de[i]) + '<p>' + JSON.stringify(obj_de[i + 1]) + '<p>' + JSON.stringify(obj_de[i + 2]) +'<button onclick="removeBook('+ point +')" class="b2" id="' + i + '" type="submit" >Remove Book</button>'+ '<button onclick="modifyBook('+ point +')" class="b3" id="' + i + '" type="submit" >Modify Book</button>'+'</div>';
        document.querySelector('#fav').insertAdjacentHTML('beforeend', html1);
    }
}

function removeBook(point){
    
   
    localStorage.removeItem('data' + point);
    location.reload();

    
}

function modifyBook(point){
  
    url ='/ModifyBook?param='+point;    
    location.replace(url);

    
}

document.addEventListener('DOMContentLoaded',()=>{
    const queryString = window.location.search;

    const urlParams = new URLSearchParams(queryString);

    const page_point = urlParams.get('param')

    

    global_point = page_point;
})

function modifyBook1(){
   
    
    
    var modify_author = document.querySelector('#author').value;
    var modify_title = document.querySelector('#title').value;
    
    let bid = JSON.parse(localStorage.getItem('data' + global_point)).bookid;
    
    
    let obj = {author:modify_author,title:modify_title,bookid:bid};
    
    let json_obj = JSON.stringify(obj);
    localStorage.setItem('data'+ global_point,json_obj);
    location.replace('/MyBooks');

    

}