import Vue from 'vue'
import VueRouter from 'vue-router'

import Login from '../views/Login/Login.vue'
import ToUserId from '../views/ToUserId/ToUserId.vue'

import Register from "@/views/Register/Register";


Vue.use(VueRouter)

  const routes = [

    {
      path:"/login",
      name:"Login",
      component:Login
    },
    {
      path:"/",
      name:"ToUserId",
      component:ToUserId,
      meta:{ requiresAuth : true}
    },


    {
      path:"/register",
      name:"Register",
      component:Register
    },




  ]

const router = new VueRouter({
  routes
})

export default router
