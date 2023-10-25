<template>
  <a-dropdown placement="bottomRight">
    <span class="ant-pro-account-avatar">
      <a-avatar size="small" :src="greetImg" class="antd-pro-global-header-index-avatar" />
      <span>{{ currentUserName }}</span>
    </span>
    <template v-slot:overlay>
      <a-menu class="ant-pro-drop-down menu" :selected-keys="[]">

        <a-menu-item v-if="$access('ENT_C_USERINFO')" key="settings" @click="handleToSettings">
          <a-icon type="setting" />
          账户设置
        </a-menu-item>

        <a-menu-divider />

        <a-menu-item key="logout" @click="handleLogout">
          <a-icon type="logout" />
          退出登录
        </a-menu-item>

      </a-menu>
    </template>
  </a-dropdown>
</template>
<script>

</script>
