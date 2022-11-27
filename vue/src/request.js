import axios from 'axios'


const service = axios.create({
    //url = base url + reqeust url
    baseURL : "http://"+SERVER.SERVER_SOCKET,

    //配置请求超时时间
    timeout: 10000
})



export default service
