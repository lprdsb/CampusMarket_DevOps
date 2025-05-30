<template>
  <div class="chat-table-container">
    <div v-if="loading" class="loading">加载中...</div>

    <el-table :data="chatList" v-if="!loading" stripe border :header-cell-style="headerStyle" :row-style="rowStyle">
      <el-table-column label="接收者" prop="receiverName" width="180">
        <template #default="{ row }">
          <el-button @click="goToChatPage(row.receiverId)" type="text" class="receiver-btn">
            {{ row.receiverName }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="最后消息" prop="lastMessage">
        <template #default="{ row }">
          <span class="last-message" :title="row.lastMessage">{{ row.lastMessage }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最后消息时间" prop="lastMessageTime" width="160">
        <template #default="{ row }">
          <span class="time">{{ formatTime(row.lastMessageTime) }}</span>
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
    formatTime(time) {
      if (!time) return '';
      const date = new Date(time);
      return date.toLocaleString();
    },
    goToChatPage(receiverId) {
      // 这里写跳转聊天页面的逻辑
      this.$router.push({ name: 'Chat', params: { id: receiverId } });
    },
    // 获取该 senderId 所有接收者的聊天记录
    async loadChatList() {
      this.loading = true;
      try {
        // 发起请求获取聊天记录
        const currentId=await this.$axios.post('/chatTable/queryCurrentUser')
        const chatterQueryDto = {};
        const response = await this.$axios.post(`/chatTable/queryUser`,chatterQueryDto);
        if (response.data.code === 200) {
          const data = response.data.data;
          this.chatList = data
              .filter(item => item.receiverId != item.senderId)
              .map((item) => {
            // 处理每个接收者的聊天记录数据
                const receiverId = item.receiverId == currentId.data ? item.senderId : item.receiverId;
                const receiverName = item.receiverId == currentId.data ? item.senderName : item.receiverName;
            return {
              receiverId: receiverId,
              receiverName: receiverName,
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
  },
  computed: {
    headerStyle() {
      return {
        backgroundColor: '#2c69f0',
        color: 'white',
        fontWeight: '600'
      };
    },
    rowStyle({ rowIndex }) {
      return rowIndex % 2 === 0 ? { background: '#f5faff' } : {};
    }
  }
};
</script>

<style scoped lang="scss">
$primary-blue: #2c69f0;
$light-blue-bg: #e6f0ff;
$text-dark: #1a3a8f;
$text-muted: #6780b3;
$row-hover: #d9e7ff;

.chat-table-container {
  padding: 20px;
  background-color: $light-blue-bg;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(44, 105, 240, 0.15);
}

.loading {
  text-align: center;
  font-size: 18px;
  color: $text-muted;
  padding: 40px 0;
}

.el-table {
  background-color: white;
  border-radius: 10px;
  overflow: hidden;

  .el-table__header-wrapper {
    background-color: $primary-blue;
    color: white;
    font-weight: 600;
    font-size: 14px;
  }

  .el-table__body-wrapper {
    font-size: 14px;
    color: $text-dark;
  }
}

.receiver-btn {
  color: $primary-blue;
  font-weight: 600;
  padding: 0;
  text-transform: none;

  &:hover {
    background-color: transparent;
    color: darken($primary-blue, 10%);
    text-decoration: underline;
  }
}

.last-message {
  display: inline-block;
  max-width: 300px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: $text-muted;
}

.time {
  color: $text-muted;
  font-size: 12px;
}

</style>