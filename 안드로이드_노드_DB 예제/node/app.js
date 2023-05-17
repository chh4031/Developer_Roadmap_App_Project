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
    console.log('받은 데이터:', receivedData);
    console.log(receivedData)
    res.send('데이터 전송됨.');

    //회원가입을 위한 코드
    const add = await connectDB.query(
      "insert into user(user_name, user_id, user_pw) values(?,?,?)", 
      [receivedData.singup_name, receivedData.singup_id, receivedData.singup_pw]);

  }catch(error){
    console.log("error");
  }
});


// Express 서버 시작
http.createServer(app).listen(app.get('port'), function(){
    console.log('익스프레스 서버를 시작했습니다 : ' + app.get('port'));
});


module.exports = app;
