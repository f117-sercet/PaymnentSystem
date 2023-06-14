
<template>

</template>

<script>
// 分发器页面，  用户扫码后进入的第一个页面：
// 判断是否存在channelUserId, 如果存在则直接跳到 对应的支付页面，如果不存在则重定向到对应的页面。
import {getRedirectUrl} from "../api/api";
import {ref} from "vue";
import config from "../config";

export default {
  components:{

  },
  mounted() {

    console.log("正在跳转",this.$router.params)

    // eslint-disable-next-line no-constant-condition
    // if( true ){
    //   this.$router.push({name: "CashierAlipay"})
    //   return;
    // }


    //TODO 需要获取到不同的商户的userId
    // if(channelUserId.getChannelUserId()){// 存在
    //
    //   //重定向到对应的支付页面
    //   this.$router.push({name: wayCodeUtils.getPayWay().routeName})
    //   return ;
    //
    // }

    const that =this;
    // 以下为不存在
    getRedirectUrl().then(res =>{
      location.href = res;
    }).catch(res =>{
      that.$router.push({name:config.errorPageRouteName,params:{errInfo:res.msg}})
    });
  }
}
</script>

<style lang="css" scoped>
.jump {
  height:100vh;
  width: 100vw;
  display:flex;
  justify-content: center;
  align-items: center;
}
</style>
