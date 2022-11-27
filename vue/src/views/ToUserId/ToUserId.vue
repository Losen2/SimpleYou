<template>
  <el-container class="body">
    <el-drawer
        :direction="direction"
        title="我是标题"
        :visible.sync="drawer"
        :with-header="false">

      <el-avatar shape="square" :size="100" :src="user['head_img']" style="margin: 20px">

      </el-avatar>
      <el-descriptions title="用户信息" column="1" style="margin: 20px">
        <el-descriptions-item label="用户名">{{ user['name'] }}</el-descriptions-item>
        <el-descriptions-item label="id">{{ user['id'] }}</el-descriptions-item>
      </el-descriptions>


      <el-upload
          ref="avaterupload"
          class="upload-demo"
          action="fakeaction"
          :http-request="uploadAvater"
          accept=".png,.jpeg,.jpg,.gif"
          :limit="1"
          :show-file-list="false"
      >
        <el-button type="primary" plain style="margin: 20px;" @click="clearAvaterFiles">上传头像</el-button>

        <!--      <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>-->
      </el-upload>


      <br>
      <el-button type="danger" plain style="margin: 20px;" @click="loginOut">退出登录</el-button>


    </el-drawer>
    <el-container class="left" direction="vertical">
      <el-container class="AvaterHeader" style="border:none">
          <span class="el-dropdown-link" @click="drawer=true" style="cursor: pointer">
                <el-avatar :size="60" :src="user['head_img']" class="useravater"
                ></el-avatar>
          </span>

        <div
            style="position:relative;left: 20px;top:30%;height: 30%;color: white;font-size: 150%;">
          {{ user.name }}
        </div>
        <el-popover
            placement="right"
            width="400"
            trigger="click">
          <div style="font-size: 120%">添加好友</div>
          <el-input v-model="searchid" placeholder="输入id搜索用户" style="margin: 10px 10px 10px 0;"></el-input>
          <el-button @click="selecttofriend(searchid);searchid=''">搜索</el-button>
          <el-divider></el-divider>
          <div v-if="selectuser['id']!==undefined">
            <div>
              <span>
          <el-avatar shape="circle" :size="60" :src="selectuser['head_img']" style="position: relative;margin: 20px;">

          </el-avatar>
                <el-button v-if="!isFriend" style="position: relative;left: 30%" @click="addFriend(selectuser['id'])">添加好友</el-button>
                <el-button v-if="isFriend" disabled="'true'" style="position: relative;left: 30%">已添加</el-button>
              </span>

              <el-descriptions title="用户信息" column="2" style="position: relative;margin: 20px">
                <el-descriptions-item label="用户名">{{ selectuser['name'] }}</el-descriptions-item>
                <el-descriptions-item label="id">{{ selectuser['id'] }}</el-descriptions-item>
              </el-descriptions>
            </div>
            <!--  anslist-->
          </div>

          <!--          弹出按钮-->
          <el-button @click="selectuser={}" type="text" icon="el-icon-plus"
                     style="position: absolute;font-size: 120%;color: #FFFFFF;top: 20%;right: 10%;border: none"
                     slot="reference"></el-button>
        </el-popover>
      </el-container>
      <el-container v-if="activeChatOrContacts == 'chat'" class="Chatings" direction="vertical">
        <!--将来用v-for代替-->

        <li v-for="usersession in usersessions" class="sessionlist"
            style="border: none;position: relative;cursor: pointer"
            @click="curtouser = usersession;defaultwordsswitch=false;getMessages(usersession['id']);">


          <el-avatar :size="50" :src="usersession['head_img']" class="touseravater" style="border: none"></el-avatar>

          <div class="sessionname" style="border: none">{{ usersession['name'] }}</div>
          <div v-if="lastMessages[usersession['id']]!==undefined" style="border: none;position: relative;left: 80px;bottom:25px;width:120px;color: #666666">{{lastMessages[usersession['id']].slice(0,5)}}</div>
          <!--          <div v-else style="border: none;position: relative;left: 80px;bottom:25px;width:120px;color: #666666">测试数据</div>-->

          <el-badge :hidden="unreadCounts[usersession.id]===0" :value="unreadCounts[usersession.id]" style="position: relative;left:80%;bottom: 50%;border: none">
          </el-badge>


        </li>


        <!--      <el-divider></el-divider>-->


      </el-container>
      <el-container style="" v-if="activeChatOrContacts == 'contact'" class="Contacts"
                    direction="vertical">
        <!--将来用v-for代替-->
        <!--        <div style="  position: relative;height:15%;overflow-y: scroll;overflow-x: hidden;">-->
        <!--          <li v-for="i in (1,100)">-->
        <!--            test{{i}}-->
        <!--          </li>-->
        <!--        </div>-->
        <el-container style="position: relative;top:0;height: 7%;border-bottom: 1px darkgray solid">
          <el-dialog title="好友申请管理" :visible.sync="dialogFriendVisible">
            <el-tabs stretch="true" v-model="activeApplyOrApplied" style="  margin: 10px;
  position: relative;
  ;">
              <el-tab-pane label="我发出的申请" name="apply"></el-tab-pane>
              <el-tab-pane label="我收到的申请" name="applied"></el-tab-pane>
            </el-tabs>
            <div v-if="activeApplyOrApplied=='apply'">

              <div style="position: relative;overflow-y: scroll;overflow-x: hidden;height: 400px">
                <div v-for="apply in applyList" style="margin: 20px">
                  <el-container>
                    <el-avatar :size="50" :src="apply['appliedUser']['head_img']"
                               style="border: none"></el-avatar>
                    <p style="position: relative">用户名：{{apply['appliedUser']['name']}}</p>
                    <p style="position: relative;left: 20px">用户id：{{apply['appliedUser']['id']}}</p>
                    <el-button v-if="apply['acceptOrRefuse']===-1" disabled="'true'" type="text" style="position:absolute;right: 20%">申请中</el-button>
                    <el-button v-if="apply['acceptOrRefuse']===0" disabled="'true'" type="text" style="position:absolute;right: 20%">被拒绝</el-button>
                    <el-button v-if="apply['acceptOrRefuse']===1" disabled="'true'" type="text" style="position:absolute;right: 20%">已接受</el-button>

                  </el-container>




                </div>
              </div>
            </div>
            <div v-if="activeApplyOrApplied=='applied'">
              <div style="position: relative;overflow-y: scroll;overflow-x: hidden;height: 400px">
                <div v-for="apply in appliedList" style="margin: 20px">
                  <el-container>
                    <el-avatar :size="50" :src="apply['applyUser']['head_img']"
                               style="border: none"></el-avatar>
                    <p style="position: relative">用户名：{{apply['applyUser']['name']}}</p>
                    <p style="position: relative;left: 20px">用户id：{{apply['applyUser']['id']}}</p>
                    <el-button v-if="apply['acceptOrRefuse']===-1" type="text" @click="receiveapply(apply)" style="position:absolute;right: 20%">同意</el-button>
                    <el-button v-if="apply['acceptOrRefuse']===-1" type="text" @click="rejectapply(apply['id'])" style="position:absolute;right: 10%;color: #FF0000">拒绝</el-button>
                    <el-button v-if="apply['acceptOrRefuse']===0" disabled="'true'" type="text" style="position:absolute;right: 20%">已拒绝</el-button>
                    <el-button v-if="apply['acceptOrRefuse']===1" disabled="'true'" type="text" style="position:absolute;right: 20%">已接受</el-button>

                  </el-container>




                </div>
              </div>
            </div>
          </el-dialog>
          <el-button type="text" style="position: relative;left:10px;cursor: pointer;color: #2c3e50;font-size: 120%;"
                     @click="dialogFriendVisible=true;getApplyAndAppliedList();">新的朋友
          </el-button>
          <div style="position:relative;left: 30%;top: 27%;height:50%;">
            <el-badge :hidden="friendapplycount===0" :value="friendapplycount" class="item">
            </el-badge>
          </div>
          <el-button icon="el-icon-arrow-right" type="text"
                     style="position: absolute;right:3%;cursor: pointer;color: #2c3e50;font-size: 120%;"
                     @click="dialogFriendVisible=true;getApplyAndAppliedList();"></el-button>

        </el-container>
        <div style="
  position: relative;
  height: 10%;
  ;">
          <el-tabs stretch="true" v-model="activeFriendsOrGroup" @tab-click="handleClick" style="  margin: 10px;
  position: relative;
  ;">
            <el-tab-pane label="好友" name="friends"></el-tab-pane>
            <el-tab-pane label="群聊" name="groups"></el-tab-pane>
          </el-tabs>
        </div>
        <div v-if="activeFriendsOrGroup=='friends'"
             style="position: relative;overflow-y: scroll;overflow-x: hidden;height: 83%">
          <li v-for="friend in friendList" class="friends" style="cursor: pointer"
              @click="createUserSession(friend['id']);curtouser = friend;defaultwordsswitch=false;getMessages(friend['id']);">
            <el-avatar :size="50" :src="friend['head_img']" class="touseravater"></el-avatar>
            <div class="friendname" style="position: relative;">{{ friend['name'] }}</div>
          </li>
        </div>
        <div v-if="activeFriendsOrGroup=='groups'"
             style="position: relative;overflow-y: scroll;overflow-x: hidden;height: 83%">
          <li v-for="i in 20" class="groups" style="cursor: pointer">
            <el-avatar :size="50" :src="circleUrl" class="touseravater"></el-avatar>
            <div class="groupname">群聊开发中</div>
          </li>
        </div>


      </el-container>
      <el-container class="Tabs" style="background:#F2F6FC">

        <div class="chattab">

          <el-badge :hidden="sumunreadcount===0" :value="sumunreadcount" style="left:25%;top:25%">
            <el-button style="font-size:100%;padding: 0;border: none;background:rgba(0,0,0,0);"
                       plain autofocus="true" icon="el-icon-chat-dot-round" @click="changeChat"></el-button>
          </el-badge>
        </div>
        <div class="persontab">

          <el-badge :hidden="friendapplycount===0" :value="friendapplycount" style="left:25%;top:25%">
            <el-button style="font-size:100%;padding: 0;border: none;background:rgba(0,0,0,0);"
                       plain icon="el-icon-user" @click="changeContacts"></el-button>
          </el-badge>
        </div>


      </el-container>

    </el-container>


    <el-container class="right" v-show="defaultwordsswitch">
      <div class="DefaultWords">
        SIMPLE<br>&ensp;&nbsp;YOU

      </div>
    </el-container>
    <transition name="el-zoom-in-top">
      <el-container class="rightwithmessage" v-if="!defaultwordsswitch" direction:vertical>
        <el-dialog title="详细信息" :visible.sync="dialogTableVisible">
          <el-avatar :size="50" :src="curdisplayuser['head_img']" class="touseravater"></el-avatar>
          <el-descriptions title="用户信息" column="1" style="margin: 20px">
            <el-descriptions-item label="用户名">{{ curdisplayuser['name'] }}</el-descriptions-item>
            <el-descriptions-item label="id">{{ curdisplayuser['id'] }}</el-descriptions-item>
          </el-descriptions>
        </el-dialog>

        <el-container style="position: absolute;
  top: 0%;
  left: 0%;
background: #409EFF;
  width: 100%;
  height: 10%;

  /* padding-bottom: 40px; */
  border-bottom: 1px #fff solid;">
          <el-button type="text" icon="el-icon-arrow-left" style="font-size: 200%;color: #FFFFFF"
                     @click="defaultwordsswitch=true;curtouser={}"></el-button>
          <div style="position:relative;left: 37%;top:37%;height: 30%;color: white;font-size: 150%;">
            {{ curtouser['name'] }}
          </div>
        </el-container>
        <div
            id="msgcontainer"
            style="position: absolute;
  top: 10%;
  left: 0%;
  bottom: 0;
  width: 100%;
  height: 80%;
  overflow-y: scroll;
  overflow-x: hidden;
  border-left: 1px darkgray solid;
  /* padding-bottom: 40px; */
  border-bottom: 1px #fff solid;">

          <div v-for="msg in curMessages" style="  position: relative;margin: 20px;
  width: 100%;">
            <!--          <el-image-->
            <!--              style="width: 100px; height: 100px"-->
            <!--              :src="url"-->
            <!--              :preview-src-list="srcList">-->
            <!--          </el-image>-->
            <el-container style="position: relative;">

            <span @click="dialogTableVisible=true;curdisplayuser=friendMap[msg.userId]" style="cursor: pointer">
            <el-avatar shape="circle" :size="50" :src="friendMap[msg.userId]['head_img']" style="margin: 10px">

            </el-avatar>
            </span>
              <el-card style="position:relative;width:fit-content">
                <el-image v-if="msg['imgUrl']!==null&&msg['imgUrl']!==''&&msg['imgUrl']!==undefined"
                          :src="msg['imgUrl']" style="max-width: 200px"
                          :preview-src-list="[msg['imgUrl']]">
                </el-image>
                <div>{{ msg['content'] }}</div>
              </el-card>
              <div style="position: relative;border: none;width: 30px">
                <!--                <i v-if="msg['userId']===user['id']&&msg['temp']===1" class="el-icon-loading" style="position:relative;top: 65%;left: 5px"></i>-->
                <!--                <i v-if="msg['userId']===user['id']&&msg['temp']===0" class="cubeic-round-border" style="position:relative;top: 65%;left: 5px"></i>-->
                <!--              <i class="el-icon-circle-check" style="position:relative;top: 65%;left: 5px;color: green"></i>-->
                <!--              {{msg['temp']}}-->
                <!--                组件不给力-->
              </div>
            </el-container>

          </div>
        </div>
        <div style="
      position: relative;
      top: 90%;
      height: 10%;
      width: 100%;
      ">
          <el-container style="
  position: relative;
  top: 0;
  height: 50%;
  width: 100%;
  ">
            <input style="
  position: relative;
  height: 100%;
  border: 1px #c8c8c8 solid;
width: 85%" v-model.lazy="textarea" maxlength=100
                   placeholder="  输入消息"></input>

            <cube-button style="
  position: relative;
  right: 0;
  width: 15%;
  height: 100%;
  background-color: #0066FF;
  border-radius: 10px;" @click="sendMsg('')">发送
            </cube-button>
          </el-container>
          <el-container style="
  position: relative;
  bottom: 0;
  height: 50%;
  width: 10%;
">

            <el-upload
                ref="pictureupload"
                class="upload-demo"
                action="fakeaction"
                :http-request="uploadObs"
                accept=".png,.jpeg,.jpg,.gif"
                :limit="1"
                :show-file-list="false"
                :file-list="fileList"
                style="position:relative;
bottom: 0;
  height: 100%;
  width: 100%;
  border: none"
            >
              <el-badge :value="1" style="left:50%;top:10px;border: none;" hidden>
                <el-button style="font-size:150%;margin:0;padding: 0;border: none;background:rgba(0,0,0,0);"
                           plain autofocus="true" icon="el-icon-picture-outline" @click="clearPicture"></el-button>
              </el-badge>
              <!--                  <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>-->
            </el-upload>
          </el-container>
        </div>


      </el-container>
    </transition>
  </el-container>
</template>
<script>
import axios from '@/request'
import ObsClient from "_esdk-obs-browserjs@3.22.3@esdk-obs-browserjs/src/obs";
import {nextTick} from "vue";


export default {
  data() {
    return {

      dialogFriendVisible: false,
      defaultwordsswitch: true,
      direction: "ltr",
      drawer: false,
      activeChatOrContacts: 'chat',
      activeFriendsOrGroup: 'friends',
      activeApplyOrApplied: 'apply',
      // 选项相关的
      user: {},
      friendList: [],//id name head_img
      friendMap: {},//为了简单 包含本人
      dialogTableVisible: false,//详情页
      usersessions: [],//子元素 id name head_img
      curtouser: {},//id name head_img
      curMessages: [],//动态的，可扩容的

      textarea: "",//以后可能会改成键值对的形式
      temp_id_flag: 0,//做标记的
      curdisplayuser: {},//详情页展示用的
      applyList: [],
      appliedList: [],
      searchid: '',//添加好友的id
      isFriend: false,
      selectuser: {},//搜索到的对象,

      recordlength:0,//管滚动的
      maxLengthofCurmessage:300,//管滚动显示的
      friendapplycount:0,//下面联系人和新的朋友那里的角标数字
      lastMessages:{},//touserid:最后一条消息这种格式 用于展示 pullMessages的时候初始化 然后随着sendmessage wsonmessage和createsession更新
      unreadCounts:{},//touserid:unreadcount这样，想个办法在首次进入的pulling时候从后端获取这些数，然后前端根据websocketonmessage动态更新
      sumunreadcount:0
    };
  },

  methods: {


    receiveapply(friendApply)
    {
      let thus = this
      axios.get('/receiveapply' + '?friendApplyId=' + friendApply.id,
          {
            headers: {
              "token": thus.$store.state.token
            }
          }).then(function (response) {
        thus.$notify({
          title: '成功',
          message: '成功添加好友',
          duration: 2000,
          position: "top-right",
          customClass: 'notify-success'
        });
        thus.dialogFriendVisible = false
        thus.getFriendList()//9.29新增
        //搞完之后给后端发我已收到请求，后端判断用户是否在线，如果在线就实时提醒不在线就无事发生
        let data= {
          userId: thus.user.id,
          toUserId: friendApply.applyUserId,
          update: 3,
          temp:0
        }
        thus.websock.send(JSON.stringify(data))
        console.log("已发送接收邀请的receiveapply")
        console.log(data)
        thus.getApplyAndAppliedList();
      })

    },
    rejectapply(friendApplyId){
      let thus = this
      this.$confirm('是否拒绝该用户的好友申请?', '提示', {
        confirmButtonText: '拒绝',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let thus = this
        axios.get('/rejectapply' + '?friendApplyId=' + friendApplyId,
            {
              headers: {
                "token": thus.$store.state.token
              }
            }).then(function (response) {
          thus.$notify({
            title: '成功',
            message: '已拒绝',
            duration: 2000,
            position: "top-right",
            customClass: 'notify-success'
          });
          thus.dialogFriendVisible = false
        }).catch(() => {

        });




      })
      thus.getApplyAndAppliedList();
    },
    addFriend(id) {
      let thus = this
      axios.get('/applyforid' + '?toUserId=' + id,
          {
            headers: {
              "token": thus.$store.state.token
            }
          }).then(function (response) {

        if (response.data.errDesc === "applyExist") {
          thus.$notify.error({
            title: '错误',
            message: '请勿重复发送请求',
            duration: 2000,
            position: "top-right",
            customClass: 'notify-error'
          });
        } else if (response.data.msg === "请求已发送") {
          thus.$notify({
            title: '成功',
            message: '已发送请求',
            duration: 2000,
            position: "top-right",
            customClass: 'notify-success'
          });

          let data= {
            userId: thus.user.id,
            toUserId: id,
            update: 5,
            temp:0
          }

          thus.websock.send(JSON.stringify(data))
        }


      })


    },
    selecttofriend(id) {
      let thus = this
      axios.get('/selecttofriend' + '?id=' + id,
          {
            headers: {
              "token": thus.$store.state.token
            }
          }).then(function (response) {

        if (response.data.errDesc === "noPerson") {
          thus.$notify.error({
            title: '错误',
            message: '用户不存在',
            duration: 2000,
            position: "top-right",
            customClass: 'notify-error'
          });

        } else if (response.data.errDesc === "donotserachself") {
          thus.$notify.error({
            title: '错误',
            message: '不要搜你自己',
            duration: 2000,
            position: "top-right",
            customClass: 'notify-error'
          });
        } else {
          if (response.data.msg === "好友") {
            thus.isFriend = true
          } else if (response.data.msg === "陌生人") {
            thus.isFriend = false
          }
          thus.selectuser = response.data.data;

        }


      })
    },
    getApplyAndAppliedList() {
      console.log("getting2lists")
      let thus = this
      axios.get('/getfriendapplylist',
          {
            headers: {
              "token": thus.$store.state.token
            }
          }).then(function (response) {
        thus.applyList = response.data.data
        axios.get('/getfriendappliedlist',
            {
              headers: {
                "token": thus.$store.state.token
              }
            }).then(function (res) {
          thus.appliedList = res.data.data
          console.log("twolists")
          console.log(thus.applyList)
          console.log(thus.appliedList)
          //计算未答复的请求
          //friendapplycount
          thus.friendapplycount = 0//归零先
          thus.appliedList.forEach((apply, index) => {
            if(apply.acceptOrRefuse===-1)
              thus.friendapplycount++
          })
          thus.applyList.forEach((apply, index) => {
            if(apply.acceptOrRefuse===-1)
              thus.friendapplycount++
          })

        })
      })

    },
    uploadObs(fileParams) {
      // 创建ObsClient实例
      let upfile = fileParams.file
      let thus = this
      let url = upfile.name
      var obsClient = new ObsClient({
        access_key_id: OBS_CONFIG.access_key_id, // 你的ak
        secret_access_key: OBS_CONFIG.secret_access_key, // 你的sk
        server: OBS_CONFIG.server // 你的endPoint
      })
      var callback = function (transferredAmount, totalAmount, totalSeconds) {
        // // 获取上传平均速率（KB/S）
        // console.log(transferredAmount * 1.0 / totalSeconds / 1024);
        // 获取上传进度百分比
        console.log(transferredAmount * 100.0 / totalAmount);
        // 百分比取整数
        // console.log(Math.floor(transferredAmount * 100.0 / totalAmount)+"%")
        thus.uploadpercent = transferredAmount * 100.0 / totalAmount

      };


      obsClient.putObject({
        Bucket: 'fsimimg', // 桶名
        Key: url, // 路径 + 文件名
        SourceFile: upfile,
        ProgressCallback: callback
      }, function (err, result) {
        if (err) {
          console.error('Error-->' + err)
        } else {
          console.log('Status-->' + result.CommonMsg.Status)
          //回调函数

          thus.sendMsg(OBS_CONFIG.message_bucket_url + url)
        }
      })

    },

    sendMsg(img_url) {
      let thus = this
      let data = {
        id: '',

        userId: this.$store.state.userId,

        toUserId: this.curtouser['id'],

        toGroupId: '',
        content: this.textarea,
        imgUrl: img_url,
        videoUrl: '',

        fileUrl: '',
        readCount: 0,

        frontCreateTime: this.formatDateTime(new Date()),

        behindTime: 0,

        tempId: this.temp_id_flag,

        temp: 1,

        update: 0,


      }
      // await this.db.put(thus.$store.state.userId+"_temp_message", data)
      var request = indexedDB.open("FSCHATMessages");

      request.onsuccess = function (event) {
        let db = event.target.result;
        var transaction = db.transaction([thus.user.id+"_temp_message"], "readwrite");
        var objectStore = transaction.objectStore(thus.user.id+"_temp_message");
        objectStore.put(data);

      };

      this.temp_id_flag++

      this.curMessages.push(data)
      this.websock.send(JSON.stringify(data))
      this.lastMessages[this.curtouser.id] = data.content
      this.textarea = ''

    },
    initWebSocket: function (userId) {
// WebSocket与普通的请求所用协议有所不同，ws等同于http，wss等同于https
      this.websock = new WebSocket("ws://"+SERVER.SERVER_SOCKET+"/websocket/" + userId);
      this.websock.onopen = this.websocketonopen;
      this.websock.onerror = this.websocketonerror;
      this.websock.onmessage = this.websocketonmessage;
      this.websock.onclose = this.websocketclose;
    },
    websocketonopen: function () {
      console.log("WebSocket连接成功");
    },
    websocketonerror: function (e) {
      console.log("WebSocket连接发生错误", e);
    },
    async websocketonmessage(e) {
//  先检测前端显示的usersessions里是否有相应的user发过来的数据（接收数据时本方是touser），不用连后端检测因为usersession本来就不是严肃的东西
//       console.log(e.data)//e.data是一个字符串，所以要json化一下
      let thus = this
      var msg = JSON.parse(e.data)
      if(msg.update===1){//处理更新消息，更改消息状态由temp到perm
        msg['update'] = 0
        // await this.db.add(this.user['id']+"_perm_message",msg);
        // await this.db.delete(this.user['id']+"_temp_message",msg['tempId']);
        var request = indexedDB.open("FSCHATMessages");

        request.onsuccess = function (event) {
          let db = event.target.result;
          var transaction = db.transaction([thus.user['id']+"_perm_message",thus.user['id']+"_temp_message"], "readwrite");
          var objectStore = transaction.objectStore(thus.user['id']+"_perm_message");
          objectStore.add(msg)
          objectStore.delete(msg.tempId);

        };

      }

      else if(msg.update===0){//处理一般收到的的消息
        console.log("收到消息----------------")
        console.log(msg)
        console.log("curtouser------")
        console.log(thus.curtouser)
        // await this.db.add(this.user['id']+"_perm_message",msg);
        var request = indexedDB.open("FSCHATMessages");

        request.onsuccess = function (event) {
          let db = event.target.result;
          var transaction = db.transaction([thus.user['id']+"_perm_message"], "readwrite");
          var objectStore = transaction.objectStore(thus.user['id']+"_perm_message");
          objectStore.add(msg)

        };
        thus.lastMessages[msg.userId] = msg.content
        thus.$forceUpdate()
        var isExist = false//是否创建新的用户对话
        this.usersessions.forEach((usersession, index) => {
          if (usersession['id'] === msg["userId"]) {
            isExist = true;
          }
        })

        if (!isExist) {
          this.createUserSession(msg["userId"])
        }
        //
        if (this.curtouser['id'] === msg["userId"]) {
          this.curMessages.push(msg)
        }
        else {
          this.unreadCounts[msg["userId"]]++
          this.sumunreadcount++
          thus.$forceUpdate()
        }
      }
      else if(msg.update===4){//有好友申请通过

        await thus.getFriendList()
        thus.$notify({
          title: '有新朋友啦',
          message: thus.friendMap[msg["userId"]].name+'已通过你的申请',//这里后端并没有对userid和touserid更改 所以看起来会有不适感
          duration: 2000,
          position: "top-right",
          customClass: 'notify-success'
        });

      }
      else if(msg.update===6){//有新的好友申请
        thus.$notify({
          title: '系统通知',
          message: '您有一条新的好友申请，请查看',//这里后端并没有对userid和touserid更改 所以看起来会有不适感
          duration: 2000,
          position: "top-right",
          customClass: 'notify-success'
        });
        //前端显示更新，这里有个复用的写法是调用一次getApplyAndAppliedList()
        thus.friendapplycount++
        //

      }

    },
    websocketclose: function (e) {

      console.log(e);

    },
    formatDateTime(date) {
      var y = date.getFullYear();
      var m = date.getMonth() + 1;
      m = m < 10 ? ('0' + m) : m;
      var d = date.getDate();
      d = d < 10 ? ('0' + d) : d;
      var h = date.getHours();
      h = h < 10 ? ('0' + h) : h;
      var minute = date.getMinutes();
      minute = minute < 10 ? ('0' + minute) : minute;
      var second = date.getSeconds();
      second = second < 10 ? ('0' + second) : second;
      return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
    },


    getMessages(touserid) {
      let thus = this
      //显示更新
      thus.sumunreadcount -= thus.unreadCounts[touserid]
      thus.unreadCounts[touserid] = 0
      thus.$forceUpdate()
      //
      var request = indexedDB.open("FSCHATMessages");

      let msg = undefined;
      request.onsuccess = function (event) {
        //下面 获取本地最大id
        let db = event.target.result;
        var transaction = db.transaction([thus.user.id + "_perm_message"], "readwrite");
        var objectStore = transaction.objectStore(thus.user.id + "_perm_message");

        let messages1 = undefined;
        let messages2 = undefined;//对某一特定用户的发出消息和接收消息
        let now_index = objectStore.index("message_Index")
        now_index.getAll([thus.user.id,touserid]).onsuccess= function (e) {
          var data = e.target.result;
          messages1 = data.slice(-300);
          console.log("msg1")
          console.log(messages1)
          now_index.getAll([touserid,thus.user.id]).onsuccess= function (e) {
            var data = e.target.result;
            for(var i=Math.max(0,data.length-300);i<data.length;i++)
            {
              if(data[i].readCount === 0)
              {
                console.log("已读1标记修改")

                data[i].readCount = 1
                console.log(data[i])
                objectStore.put(data[i]);
              }
              //表示已读

            }
            messages2 = data.slice(-300);//节约性能，下面还要排序呢。完整的消息之后会出历史记录功能
            console.log("msg2")
            console.log(messages2)
            msg = thus.sortMessages(messages1.concat(messages2))
            console.log("msg")
            // console.log(msg)//写成这种类似回调的嵌套才能拿得到数据
            thus.curMessages = msg.slice(-thus.maxLengthofCurmessage)//最多装300条，注意引用
          };

        };
      };
      axios.get('/clearunreadcount'+'?toUserId='+touserid,
          {
            headers: {
              "token": thus.$store.state.token
            }
          })

    },
    sortMessages  (arr = []) {
      const copy = arr.slice();
      const sorter = (a, b) => {
        return a['behindTime'] - b['behindTime'];
      };
      return copy.sort(sorter);
    },
    createUserSession(touserid) {
      let thus = this
      axios.get('/createusersession' + '?touserid=' + touserid,
          {
            headers: {
              "token": thus.$store.state.token
            }
          }).then(function (response) {
        thus.getUserSession()//刷新usersessions列表
      })
    },
    getUserSession() {//实质上get回来的是List<User>
      console.log("getttuserrrrrrrsessssssssss")
      let thus = this
      axios.get('/getusersession',
          {
            headers: {
              "token": thus.$store.state.token
            }
          }).then(function (response) {
        thus.usersessions = response.data.data
        console.log("usersession")
        console.log(thus.usersessions)
      })
    },
    async getFriendList() {
      let thus = this
      thus.friendList = []//先清空 9.29
      await axios.get('/getfriendlist',
          {
            headers: {
              "token": thus.$store.state.token
            }
          }).then(function (response) {

        response.data.data.forEach((friend, index) => {
              thus.friendList.push(friend)
              thus.friendMap[friend['id']] = friend
            }
        )

        // console.log("friendlist-------------------------------------")
        // console.log(thus.friendList)
        // console.log(thus.friendMap)

      })

    },
    loginOut() {
      let thus = this
      axios.get('/loginOut',
          {
            headers: {
              "token": thus.$store.state.token
            }
          })
          .then(function (response) {
            if (response.data.code == -1) {
              return "登录错误";
            }

            //store和localstorage设置
            thus.$store.dispatch('clearToken');
            localStorage.removeItem("FSIM_USERTOKEN");
            thus.$store.dispatch('clearName');
            localStorage.removeItem("userName");
            thus.$store.dispatch('clearId');
            localStorage.removeItem("userId");
            thus.$store.dispatch('clearCurSessionId');
            localStorage.removeItem("curSessionId");
            thus.$store.dispatch('clearCurToUserId');
            localStorage.removeItem("curToUserId");


            console.log("退出登录成功");
          })
          .catch(function (error) {
            console.log(error);
          });
      thus.$router.push('/login');
      location.reload();//这个要加不然还得手动刷新
    },
    avaterUploadSuccess() {

      this.$message('上传头像成功')


    },
    changeChat() {
      this.activeChatOrContacts = 'chat'
    },
    changeContacts() {
      this.activeChatOrContacts = 'contact'
    },
    handleClick(tab, event) {
      console.log(this.activeName);
      // 选项相关的
    },
    clearAvaterFiles() {
      this.$refs['avaterupload'].clearFiles();
      console.log("头像已清空")
    },
    clearPicture() {
      this.$refs['pictureupload'].clearFiles();
      console.log("上传文件已清空")
    },
    uploadAvater(fileParams) {
      // 创建ObsClient实例
      let upfile = fileParams.file
      let thus = this
      let url = upfile.name
      var obsClient = new ObsClient({
        access_key_id: OBS_CONFIG.access_key_id, // 你的ak
        secret_access_key: OBS_CONFIG.secret_access_key, // 你的sk
        server: OBS_CONFIG.server // 你的endPoint
      })
      var callback = function (transferredAmount, totalAmount, totalSeconds) {
        // // 获取上传平均速率（KB/S）
        // console.log(transferredAmount * 1.0 / totalSeconds / 1024);
        // 获取上传进度百分比
        console.log(transferredAmount * 100.0 / totalAmount);
        // 百分比取整数
        // console.log(Math.floor(transferredAmount * 100.0 / totalAmount)+"%")
        thus.uploadpercent = transferredAmount * 100.0 / totalAmount

      };


      obsClient.putObject({
        Bucket: 'fsimavater', // 桶名
        Key: url, // 路径 + 文件名
        SourceFile: upfile,
        ProgressCallback: callback
      }, function (err, result) {
        if (err) {
          console.error('Error-->' + err)
        } else {
          console.log('Status-->' + result.CommonMsg.Status)


        }
      });


      thus.uploadUserAvaterUrl(url)
      thus.avaterUploadSuccess()


    },
    uploadUserAvaterUrl(url) {
      let thus = this
      axios.get('/updateavaterbyid?avaterUrl=' + OBS_CONFIG.avater_bucket_url + url + '&token=' + this.$store.state.token).then(
          function (response) {
            console.log("修改头像成功" + "uri为：" + url)
            thus.user['head_img'] = OBS_CONFIG.avater_bucket_url + url


          }
      )

    },
    async setUser() {
      let thus = this
      await axios.get('/selectuserbyid?id=' + this.$store.state.userId + '&token=' + this.$store.state.token).then(
          function (response) {
            thus.user = response.data.data
            console.log('this.userrrrrrrrrrrrrrrr')
            console.log(thus.user)
            thus.friendMap[thus.$store.state.userId] = thus.user
          },
      )

    },


    async createDB(){


      console.log("creatinggggggggggggorloading indexdb")
      let thus = this;

      if(thus.user.id===undefined){
        await thus.setUser()
      }


      let indexedDB = window.indexedDB || window.mozIndexedDB || window.webkitIndexedDB || window.msIndexedDB;
      if (!indexedDB) {
        console.log("浏览器不支持indexedDB");
      }
// 打开我们的数据库,使用open方法

      var request = indexedDB.open("FSCHATMessages",thus.$store.state.indexdbVersion);

      request.onupgradeneeded = function (event) {
        var db = event.target.result // 数据库对象
        let objectStore

        if (!db.objectStoreNames.contains(thus.$store.state.userId+"_temp_message")) {
          objectStore = db.createObjectStore(thus.$store.state.userId+"_temp_message", { keyPath: 'tempId' }) // 创建表
          objectStore.createIndex("userid_index", "userId", { unique: false });

        }
        if (!db.objectStoreNames.contains(thus.$store.state.userId+"_perm_message")) {
          objectStore = db.createObjectStore(thus.$store.state.userId+"_perm_message", { keyPath: 'behindTime' }) // 创建表
          objectStore.createIndex("userid_index", "userId", { unique: false });
          objectStore.createIndex("touserid_index", "toUserId", { unique: false });
          objectStore.createIndex("id_index", "id", { unique: true });
          objectStore.createIndex("update_index", "update", { unique: false });
          objectStore.createIndex("readcount_index", "readCount", { unique: false });
          objectStore.createIndex("message_Index", ["userId","toUserId"], { unique: false });
        }
      };
      request.onsuccess = function (event) {
        var db = event.target.result // 数据库对象
        let objectStore
        if (!db.objectStoreNames.contains(thus.$store.state.userId+"_temp_message")) {
          objectStore = db.createObjectStore(thus.$store.state.userId+"_temp_message", { keyPath: 'tempId' }) // 创建表
          objectStore.createIndex("userid_index", "userId", { unique: false });

        }
        if (!db.objectStoreNames.contains(thus.$store.state.userId+"_perm_message")) {
          objectStore = db.createObjectStore(thus.$store.state.userId+"_perm_message", { keyPath: 'behindTime' }) // 创建表
          objectStore.createIndex("userid_index", "userId", { unique: false });
          objectStore.createIndex("touserid_index", "toUserId", { unique: false });
          objectStore.createIndex("id_index", "id", { unique: true });
          objectStore.createIndex("update_index", "update", { unique: false });
          objectStore.createIndex("readcount_index", "readCount", { unique: false });
          objectStore.createIndex("message_Index", ["userId","toUserId"], { unique: false });
        }
      };


      thus.pullMessages();


    },
    async pullMessages(){
      let thus = this
      //每次新进入页面时从服务器拉取消息

      if(thus.user.id===undefined){
        await thus.setUser()
      }

      var request = indexedDB.open("FSCHATMessages");


      var range = IDBKeyRange.bound(0,92345678911111)//只是一个单纯的很大的数
      request.onerror = function (event){
        thus.$router.go(0)//出现错误 连不上数据库的方案
      }
      request.onsuccess = function (event) {
        //下面 获取本地最大id
        let db = event.target.result;
        //下面这句经常出错
        var transaction = db.transaction([thus.user.id + "_perm_message"], "readwrite");
        var objectStore = transaction.objectStore(thus.user.id + "_perm_message");
        console.log("pullingggggggggggggggggggggggggggg")
        let a = objectStore.getAllKeys(range)

        let mongoId;
        a.onsuccess = function (e) {
          mongoId = e.target.result[e.target.result.length-1];
          console.log(mongoId);
          if(mongoId===undefined){mongoId=0}//本地无消息

          axios.get('/pullrecentmessages' + '?mongoId=' + mongoId,
              {
                headers: {
                  "token": thus.$store.state.token
                }
              }).then(function (response) {
            console.log(response.data.data);//将更新的消息写入indexdb
            let db1 = event.target.result;
            var transaction1 = db1.transaction([thus.user.id + "_perm_message"], "readwrite");
            var objectStore1 = transaction1.objectStore(thus.user.id + "_perm_message");
            response.data.data.forEach((message, index) => {
              objectStore1.put(message)//add不行
              // console.log("testcase")
              //统计未读消息

            })
          })

        };



      };



    },
    async initialLastMessagesAndUnreadCounts(){
      //在此之前 会话sessions列表已经更新 所以我们只需要从usersessions初始化二者就好
      //usersessions: [],//子元素 id name head_img
      let thus = this
      if(thus.user.id===undefined){
        await thus.setUser()
      }




      var request = indexedDB.open("FSCHATMessages");
      request.onsuccess = function (event) {
        //下面 获取本地最大id
        let db = event.target.result;
        var transaction = db.transaction([thus.user.id+"_perm_message"], "readwrite");
        var objectStore = transaction.objectStore(thus.user.id+"_perm_message");
        let now_index = objectStore.index("message_Index")
        //
        thus.usersessions.forEach((usersession,index)=>{
          now_index.getAll([thus.user.id,usersession.id]).onsuccess= function (e) {
            var data1 = e.target.result;
            //作非空性判断
            now_index.getAll([usersession.id,thus.user.id]).onsuccess= function (e) {
              var data2 = e.target.result;
              if(data1.length===0&&data2.length===0){}
              else if(data1.length===0){thus.lastMessages[usersession.id] = data2[data2.length-1].content}
              else if(data2.length===0){thus.lastMessages[usersession.id] = data1[data1.length-1].content}
              else if(data1[data1.length-1].behindTime<data2[data2.length-1].behindTime){
                thus.lastMessages[usersession.id] = data2[data2.length-1].content

              }else if(data1[data1.length-1].behindTime>=data2[data2.length-1].behindTime){
                thus.lastMessages[usersession.id] = data1[data1.length-1].content
              }
              //更新未读标记 统计-200条消息中的未读
              var datatojudge = data2.slice(-200)
              thus.unreadCounts[usersession.id]=0
              datatojudge.forEach((msg,index)=>{
                if(msg.readCount===0){
                  thus.unreadCounts[usersession.id]++
                }
              })
              //更新sumunreadcount
              thus.sumunreadcount += thus.unreadCounts[usersession.id]


              thus.$forceUpdate()

            };
          };


        })
        //thus.$forceUpdate()不能加在这里 注意异步
      };
      console.log("lastmessages-----------------")
      console.log(thus.lastMessages)


    },





  },
  updated() {
    //msg有更新时滚动到最下方
    if(this.curMessages.length>this.recordlength){

      var elmnt = document.getElementById("msgcontainer")
      elmnt.scrollTop = elmnt.scrollHeight
      this.recordlength = this.curMessages.length
    }
    if(this.curMessages.length>this.maxLengthofCurmessage)
    {this.curMessages = this.curMessages.slice(-this.maxLengthofCurmessage)}


  },
  created() {
    this.setUser()
    this.getFriendList()
    this.getUserSession()
    this.initWebSocket(this.$store.state.userId)
    this.createDB()
    //拉消息并整合
    //lastMessages初始化 unreadCounts初始化
    this.initialLastMessagesAndUnreadCounts()
    this.getApplyAndAppliedList();


  },
  mounted() {
  }



};
</script>

<style>


.friends {

  position: relative;
  width: 100%;
  height: 60px;
}

.groups {
  position: relative;
  width: 100%;
}

.sessionlist {
  position: relative;
  width: 100%;
  height: 100px;

}

.sessionname {
  position: relative;
  left: 80px;
  bottom: 40%;

}

.friendname {
  position: relative;
  left: 80px;
  bottom: 50%;

}

.groupname {
  position: relative;
  left: 80px;
  bottom: 40%;

}

.touseravater {
  position: relative;
  width: 100%;
  left: 15px;
  top: 15%;


}

.chattab {
  position: relative;
  width: 50%;
  height: 100%;
  font-size: 200%;
  /*border:none*/


}

.persontab {
  position: relative;
  width: 50%;
  height: 100%;
  font-size: 200%;
  border: none;


}

.DefaultWords {
  position: relative;
  left: 42%;
  top: 40%;
  font-size: 250%;
  color: #666666;
}

.AvaterHeader {
  background: #409EFF;
  position: relative;
  height: 10%;
  width: 100%;

}

.useravater {
  position: relative;
  top: 15%;
  left: 10px
}

.Chatings {
  overflow-y: scroll;
  overflow-x: hidden;
  position: relative;
  height: 80%;
  width: 100%;
}

.Contacts {

  position: relative;
  height: 80%;
  width: 100%;
}

.Tabs {
  /*background:#EBEEF5;*/
  position: relative;
  height: 10%;
  width: 100%;


}

.body {
  padding: 0;
  margin: 0;
  overflow: hidden;


}

.left {
  width: 20%;
  height: 100%;
  background: #FFFFFF;
  position: absolute;
  left: 0;
  top: 0;

}

.rightwithmessage {

  width: 80%;
  height: 100%;
  position: absolute;
  background: #FFFFFF;
  right: 0;
  top: 0;
  overflow: hidden;

  /*line-height: 60px;*/
}

.right {
  width: 80%;
  height: 100%;
  position: absolute;
  background: #DCDFE6;
  right: 0;
  top: 0;
  overflow: hidden;

  /*line-height: 60px;*/
}

/*定义滚动条高宽及背景 高宽分别对应横竖滚动条的尺寸*/
::-webkit-scrollbar {
  width: 7px;
  height: 7px;
  background-color: #f5f5f5;
}

/*定义滚动条轨道 内阴影+圆角*/
::-webkit-scrollbar-track {
  box-shadow: inset 0 0 6px rgba(0, 0, 0, 0);
  -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0);
  border-radius: 10px;
  background-color: #FFFFFF;
}

/*定义滑块 内阴影+圆角*/
::-webkit-scrollbar-thumb {
  border-radius: 10px;
  box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.1);
  -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.1);
  background-color: #c8c8c8;
}

.notify-success {
  top: 20px !important;
  right: 20px !important;
  width: 300px !important;
  height: 120px !important;
  background: rgba(255, 255, 255, 100) !important;
  border-radius: 14px 14px 14px 14px !important;
}

.notify-error {
  top: 20px !important;
  right: 20px !important;
  width: 300px !important;
  height: 120px !important;
  background: rgba(255, 255, 255, 100) !important;
  border-radius: 14px 14px 14px 14px !important;
}

</style>
