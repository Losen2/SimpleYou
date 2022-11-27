<template>
  <div class="main">
    <div class="input">
    <cube-input
        v-model="name"
        :placeholder="placeholder"

        :maxlength="maxlength"
        :autofocus="autofocus"
        :autocomplete="autocomplete"

    ></cube-input>
    </div>
    <div class="input">
      <cube-input
          v-model="pwd"
          :placeholder="placeholder1"
          :type="type"
          :maxlength="maxlength"
          :autofocus="autofocus"
          :autocomplete="autocomplete"
          :eye="eye"
      ></cube-input>
    </div>
    <cube-button @click="login">登录</cube-button>
    <router-link to="/register" class="reg">注册</router-link>
  </div>
</template>

<script>
import axios from '@/request'
//登录接口
export default {

  data() {
    return {
      name: '',
      pwd: '',
      placeholder: '请输入用户名',
      placeholder1: '请输入密码',
      type: 'password',
      maxlength: 100,
      disabled: true,
      autofocus: true,
      autocomplete: true,

      eye1: {
        open: true,
        reverse: false
      },
      eye: {
        open: false,
        reverse: false
      }
    }
  },
  methods:{


    login(){
      console.log("login函数调用")
      let thus = this
      axios.get('/login?name=' + this.name + '&pwd=' + this.pwd)
          .then(function (response) {
            console.log(response);
            if(response.data.code == null){
              console.log('调用成功，调用用户为' + response.data.data.name);
              localStorage.setItem('FSIM_USERTOKEN',response.data.data.token)


              thus.$store.dispatch('setToken',response.data.data.token)
              localStorage.setItem('userName',response.data.data.name)
              thus.$store.dispatch('setName',response.data.data.name)
              localStorage.setItem('userId',response.data.data.id)
              thus.$store.dispatch('setId',response.data.data.id)
              const toast = thus.$createToast({
                txt: "登录成功",
                type: "correct",
                time: 0
              });
              toast.show();
              setTimeout(() => {
                toast.hide();thus.$router.push('/');
              }, 2000)

            }
            else {
              const toast1 = thus.$createToast({
                txt: "登录失败",
                type: "error",
                time: 2000
              });
              toast1.show();

            }

          })
          .catch(function (error) {
            console.log(error);
          });
      },

    },



};
</script>
<style lang="scss" scoped>

.main {
  padding: 50px 5% 0;
  text-align: center;
}
.input {
  padding: 10px 0px 10px 0px;//上右下左
  text-align: center;
}
.reg {
  display: inline-block;
  margin-top: 30px;
  font-size: 18px;
}



</style>
