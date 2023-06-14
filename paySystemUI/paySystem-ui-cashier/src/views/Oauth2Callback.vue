

<template>

</template>


<script>
import {getChannelUserId} from "../api/api";
import wayCodeUtils from '@/utils/wayCode'
import channelUserIdUtil from '../utils/channelUserId'
import config from "@/config";
export default {
  components:{},
  mounted() {
   const allQuery =  Object.assign({},this.searchToObject(),this.$route.query)

    const that =this
    getChannelUserId(allQuery).then()(res =>{

      // 设置channlUserId
      channelUserIdUtil.setChannelUserId(res)

      this.$router.push({name:wayCodeUtils.getPayWay().routeName})
    }).catch(res =>{
      that.$router.push({name:config.errorPageRouteName,params:{errInfo:res.msg}})
    });
  },
  methods:{
    searchToObject:function (){

      if (!window.location.search){
        return {};
      }
      var pairs = window.location.search.substring(1).split("&"),
      result  = {},
        pair,
        i;
      for (i in pairs){
        if ( pairs[i] === "") continue;
        pair = pairs[i].split("=");
        result[ decodeURIComponent( pair[0] ) ] = decodeURIComponent( pair[1] );
      }
      return result;
    }
  }
}
</script>

<style scoped lang="css">

</style>
