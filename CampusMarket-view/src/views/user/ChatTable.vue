<template>
  <div class="chat-table-container">
    <div v-if="loading" class="loading">加载中...</div>

    <!-- 聊天列表 -->
    <el-table :data="chatList" style="width: 100%" v-if="!loading">
      <el-table-column label="接收者" prop="receiverName">
        <template slot-scope="scope">
          <el-button @click="goToChatPage(scope.row.receiverId)" type="text">{{ scope.row.receiverName }}</el-button>
        </template>
      </el-table-column>
      <el-table-column label="最后消息" prop="lastMessage">
        <template slot-scope="scope">
          <span>{{ scope.row.lastMessage }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最后消息时间" prop="lastMessageTime">
        <template slot-scope="scope">
          <span>{{ formatTime(scope.row.lastMessageTime) }}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'ChatTable',
  data() {
    return {
      senderId: '12345',  // 当前用户的 senderId, 需要根据实际情况动态获取
      chatList: [],
      loading: false,
    };
  },
  created() {
    this.loadChatList(); // 获取聊天记录
  },
  methods: {
    // 获取该 senderId 所有接收者的聊天记录
    async loadChatList() {
      this.loading = true;
      try {
        // 发起请求获取聊天记录
        const chatterQueryDto = {};
        const response = await this.$axios.post(`/chatTable/queryUser`,chatterQueryDto);
        console.log(response);
        if (response.data.code === 200) {
          const data = response.data.data;
          this.chatList = data.map((item) => {
            // 处理每个接收者的聊天记录数据
            return {
              receiverId: item.receiverId,
              receiverName: item.receiverName,
              lastMessage: item.content,  // 获取最后一条消息内容
              lastMessageTime: new Date(item.createTime.replace('年', '-').replace('月', '-').replace('日', '')),
            };
          });
        } else {
          this.$message.error(response.data.msg);
        }
      } catch (error) {
        console.error('获取聊天列表失败:', error);
        this.$message.error('加载聊天列表失败');
      } finally {
        this.loading = false;
      }
    },

    // 点击接收者，跳转到该接收者的聊天界面
    goToChatPage(receiverId) {
      this.$router.push({ path: '/chat', query: { ReceiverId: receiverId } });
    },

    // 格式化时间
    formatTime(time) {
      return time.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    }
  }
};
</script>

<style scoped>
.chat-table-container {
  padding: 20px;
}

.loading {
  text-align: center;
  font-size: 18px;
  color: #999;
}

.el-table {
  width: 100%;
}

.el-table-column {
  text-align: left;
}
</style>
