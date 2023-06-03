// Express 기본 모듈 불러오기
var express = require('express')
    , http = require('http');

// DB 정보 가져오기
const connectDB = require("./middleware/db");

// 익스프레스 객체 생성
var app = express();
app.use(express.json());
// 기본 포트를 app 객체에 속성으로 설정
app.set('port', process.env.PORT || 7878);

app.get("/", async function(req, res, next) {
  try{
    console.log("ok")
    const user = await connectDB.query("select * from user")
    console.log(user[0][0].user_id, user[0][0].user_pw)
    res.send(user[0][0].user_id + " " + user[0][0].user_pw);
  }catch(error){
    console.log("error")
  }
});

app.post('/', async (req, res) => {
  try{
    const receivedData = await req.body; //데이터를 json형식으로 가져옴.
    // console.log('받은 데이터:', receivedData);
    // console.log(receivedData)
    res.send('데이터 전송됨.');
    console.log("넘오온 데이터값 : " + receivedData.singup_id, receivedData.singup_name, receivedData.singup_pw);
    //회원가입을 위한 코드
    const add = await connectDB.query(
      "insert into user(user_name, user_id, user_pw) values(?,?,?)", 
      [receivedData.singup_name, receivedData.singup_id, receivedData.singup_pw]);

  }catch(error){
    console.log("error");
  }
});

//로그인 함수

app.post('/sign', async (req,res) => {
  try{
    let result = "";
    const {login_id, login_pw} = req.body; // 안드로이드에서 id,pw 값 가져오기
    const isuser = await connectDB.query("select * from user where user_id = ?" , [login_id]); //db에서 id값 같은거 가져오기
    let loginUser;
    let loginId;
    try{
      // loginUser = isuser[0][0].user_name;
      loginId = isuser[0][0].user_id;
    }catch(error){
      console.log("로그인 오류");
    }

    if(isuser[0].length == 0){
      result = "회원아님";
      console.log("회원아님");
      return res.send(result);
    } else{
      if(isuser[0][0].user_id == login_id && isuser[0][0].user_pw == login_pw) {
        result = "로그인 성공";
        console.log("로그인 성공");
        return res.send(loginId);
      }else if(isuser[0][0].user_id  == login_id && isuser[0][0].user_pw != login_pw || isuser[0][0].user_id  != login_id && isuser[0][0].user_pw != login_pw) {
        result = "아이디 또는 비번 틀림";
        console.log("아이디 또는 비번 틀림");
        return res.send(result);
      }
    }

  }catch(error){
    console.log(error);
  }
});

//로그인 체크
app.get('/sign' , async (req,res) => {
  try{
    res.send(check);
  }catch(error){
    console.log(error);
  }
});


// end of 로그인

// json 넘겨보기, 게시글 목록 조회 관련
app.get('/com', async (req, res) => {
  // const isuser = await connectDB.query("select * from community order by community_number desc");
  const isuser = await connectDB.query("SELECT * FROM android.user, android.community where user_user_id = user_id order by community_number desc;");
  // console.log(isuser[0]);
  try{
    res.send(isuser[0]);
    console.log("게시글 목록 조회 성공");
  }catch(error){
    console.log('게시글 목록 조회 실패');
  }
});

// 새로운 게시글 작성 통신
app.post('/create', async(req, res) => {
  try{
    const newcontent = await req.body;
    const create_content = await connectDB.query("INSERT INTO community(community_title, community_content, user_user_id) VALUES(?,?,?)",
    [newcontent.Edtitle, newcontent.Edcontent, newcontent.Eduserid]);
    console.log("게시글 작성 성공");
    res.send(newcontent.Eduserid);
  }catch(error){
    console.log("게시글 작성 실패");
  }
});

// 본인에 맞는 게시글 체크를 위한 통신
app.post('/check', async(req, res) => {
  try{
    const check_content = await req.body;
    const check = await connectDB.query("SELECT user_user_id, community_number FROM android.community WHERE community_title = ? and community_content = ?", 
    [check_content.check_title, check_content.check_content]);
    // console.log(check[0][0]);
    const check_id = check[0][0].user_user_id;
    const check_number = check[0][0].community_number;
    // console.log(check_content.check_id);

      let this_json = {
        check : "S",
        number : check_number
      };

      let no_json = {
        check : "D",
        number : 0
      };

    if (check_id == check_content.check_id){
      console.log("아이디 같음");
      res.send(this_json);
    }else{
      console.log("아이디 다름");
      res.send(no_json);
    }
  }catch(error){
    console.log("체크불가");
  }
});

//게시글 삭제를 위한 통신
app.post('/del', async(req, res) => {
  try{
    const del = await req.body;
    const del_check = await connectDB.query("DELETE FROM android.community WHERE community_title = ? and community_content = ? and user_user_id = ?", 
    [del.check_title, del.check_content, del.check_id]);
    // console.log(del.check_title, del.check_content, del.check_id);
    console.log("삭제완료");
    res.send("del_ok");
  }catch(error){
    console.log("삭제실패");
    res.send("del_fail");
  }
});

//게시글 수정을 위한 통신
app.post('/up', async(req, res) => {
  try{
    const up = await req.body;
    // console.log(up);
    const up_check = await connectDB.query("UPDATE android.community SET community_title = ?, community_content = ? WHERE community_number = ? and user_user_id = ?",
    [up.Uptitle, up.Upcontent, up.Upnumber, up.Upuserid]);
    console.log("수정완료");
    res.send(up.Upuserid);
  }catch(error){
    console.log("수정실패");
    res.send("");
  }
});

// Express 서버 시작
http.createServer(app).listen(app.get('port'), function(){
    console.log('익스프레스 서버를 시작했습니다 : ' + app.get('port'));
});


module.exports = app;
