// Express 기본 모듈 불러오기
var express = require('express')
    , http = require('http');

// DB 정보 가져오기
const connectDB = require("./middleware/db");

// 익스프레스 객체 생성
var app = express();

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

app.use(express.json());

app.post('/', async (req, res) => {
  try{
    const receivedData = await req.body; // 전송된 데이터 가져오기
    console.log('받은 데이터:', receivedData);
    // let dbData = receivedData.split(" ");
    // 아래와 같이 json형태로 데이터가 들어오는 것을 알 수 있음.
    console.log(receivedData.text)
    let dbData = receivedData.text
    console.log(dbData);
    // 추가적인 처리나 응답 작업 수행 가능
    res.send('데이터를 성공적으로 받았습니다.');
    const add = await connectDB.query(
      "insert into user(user_id, user_pw, user_name) values(?,?,?)", 
      [dbData[0], dbData[1], dbData[2]]);
  }catch(error){
    console.log("error");
  }
});


// Express 서버 시작
http.createServer(app).listen(app.get('port'), function(){
    console.log('익스프레스 서버를 시작했습니다 : ' + app.get('port'));
});


module.exports = app;
