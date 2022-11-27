import Vue from 'vue'
import Vuex from 'vuex'



Vue.use(Vuex)



export default new Vuex.Store({
  state(){
    return {
    token: localStorage.getItem("FSIM_USERTOKEN") || '',
    userId: localStorage.getItem("userId")||'',
    userName: localStorage.getItem("userName") ||'',
    curSessionId: localStorage.getItem("curSessionId") ||'',
    curToUserId:  localStorage.getItem("curToUserId") ||'',
    indexdbVersion: localStorage.getItem("indexdbVersion") ||1,
  }},
  //同步修改state里面的值
  mutations: {
    SET_ID:(state, id)=>{
      state.userId = id
    },
    SET_TOKEN:(state, token)=>{
      state.token = token
    },
    SET_NAME:(state, name)=>{
      state.userName = name
    },

    SET_CURSESSIONID:(state, curSessionId)=>{
      state.curSessionId = curSessionId
    },
    SET_CURTOUSERID:(state, curToUserId)=>{
    state.curToUserId = curToUserId
},
    SET_INDEXDBVERSION:(state,indexdbVersion)=>{
      state.indexdbVersion = indexdbVersion
    },
  },

  //异步调用mutations里面的方法
  //contxt.commit 利用上下文触发mutations某个方法
  // vue代码里面 this.$store.dispatch触发action里面的定义的方法
  actions: {
    setId(context,id){
      context.commit('SET_ID',id)
    },
    clearId(context){
      context.commit('SET_ID','')
    },
    setToken(context,token){
      context.commit('SET_TOKEN',token)
    },

    clearToken(context){
      context.commit('SET_TOKEN','')
    },
    setName(context,name){
      context.commit('SET_NAME',name)
    },

    clearName(context){
      context.commit('SET_NAME','')
    },


    setCurSessionId(context,curSessionId){
      context.commit('SET_CURSESSIONID',curSessionId)
    },
    clearCurSessionId(context){
      context.commit('SET_CURSESSIONID','')
    },
    setCurToUserId(context,curToUserId){
      context.commit('SET_CURTOUSERID',curToUserId)
    },
    clearCurToUserId(context,curToUserId){
      context.commit('SET_CURTOUSERID',curToUserId)
    },
    setIndexdbVersion(context,indexdbVersion){
      context.commit('SET_INDEXDBVERSION',indexdbVersion)
    }

  },
  modules: {
  }
})
