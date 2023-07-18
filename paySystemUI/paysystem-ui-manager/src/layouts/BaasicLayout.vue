<template>
  <pro-layout
    :menus="menus"
    :collapsed="collapsed"
    :mediaQuery="query"
    :isMobile="isMobile"
    :handleMediaQuery="handleMediaQuery"
    :handleCollapse="handleCollapse"
    :i18nRender="false"
    v-bind="settings"
    :breadcrumbRender="handleBreadcrumbRender"
    :siderWidth="210"
  >

  </pro-layout>
</template>

<script>
import appConfig from "../config/appConfig";
import {mapState} from "vuex";

export default {

  name:'BasicLayout',
  components:{
    RightContent,
    GlobalFooter
  },
  data(){

    return {

      isRouterAlive:true,
      isProPreviewSite: process.env.VUE_APP_PREVIEW === 'true' && process.env.NODE_ENV !== 'development',

      menus:[],
      collapsed:false,
      title:appConfig.APP_TITLE,
      settings:{
        // 布局类型
        layout:'sidemenu',
        contentWidth:'Fluid',//流式布局
        theme:'Light',

        //主色调
        primaryColor:'#1a53ff',//默认颜色
        fixedHeader: false, // 固定 Header
        fixSiderbar: true, // 固定左侧菜单栏
        colorWeak: false, // 色盲模式将
        hideHintAlert: false,
        hideCopyButton: false
      },
      // 媒体查询
      query:{},

      // 是否用于手机模式
      isMobile:false,
    }
  },
  computed:{
    ...mapState({
      // 动态路由
      mainMenu:state => state.asyncRouter.addRoutes
    })
  }

}
</script>
