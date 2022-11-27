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
    <div class="input">
      <cube-input
          v-model="pwdrepeat"
          :placeholder="placeholder2"
          :type="type"
          :maxlength="maxlength"
          :autofocus="autofocus"
          :autocomplete="autocomplete"
          :eye="eye"
      ></cube-input>
    </div>
    <cube-button @click="register">提交</cube-button>



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
      pwdrepeat: '',
      placeholder: '请输入用户名',
      placeholder1: '请输入密码',
      placeholder2: '请再次输入密码',
      type: 'password',
      maxlength: 100,
      disabled: true,
      autofocus: true,
      autocomplete: true,

      eye: {
        open: false,
        reverse: false
      }
    }
  },
  methods:{
    showPopup(refId) {
      const component = this.$refs[refId]
      component.show()
      setTimeout(() => {
        component.hide()
      }, 3000)
    },

    register(){

      let thus = this
      axios.get('/register?name=' + this.name + '&pwd=' + this.pwd + '&pwdrepeat='+this.pwdrepeat)
          .then(function (response) {
            console.log(response);
            if(response.data.code == null){
              const toast = thus.$createToast({
                txt: "注册成功",
                type: "correct",
                time: 0
              });
              toast.show();
              setTimeout(() => {
                toast.hide();thus.$router.push('/login');
              }, 3000)



            }
            else {
              const toast1 = thus.$createToast({
                txt: "注册失败",
                type: "error",
                time: 3000
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
.cube-popup{
  padding: 20px;
  color: 	#FF0000;
  background-color: rgba(0, 0, 0, .8);
}


.main {
  padding: 50px 5% 0;
  text-align: center;
}
.input {
  padding: 10px 0px 10px 0px;//上右下左
  text-align: center;
}



</style>
