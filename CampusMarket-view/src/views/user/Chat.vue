<template>
  <div class="chat-container">
    <!-- 顶部卖家信息栏 -->
    <div class="seller-info">
      <el-avatar :size="50" :src="seller.avatar"></el-avatar>
      <div class="seller-details">
        <h3>{{ seller.name }}</h3>
        <p>正在出售 {{ seller.productCount }} 件商品</p>
      </div>
      <el-button type="info" plain @click="backToProduct">返回商品</el-button>
    </div>

    <!-- 聊天消息区域 -->
    <div class="message-area" ref="messageArea">
      <div
          v-for="(message, index) in messages"
          :key="index"
          :class="['message-bubble', message.sender === 'me' ? 'sent' : 'received']"
      >
        <div class="message-content">
          <p>{{ message.content }}</p>
          <span class="message-time">{{ formatTime(message.time) }}</span>
        </div>
      </div>
    </div>

    <!-- 消息输入区域 -->
    <div class="input-area">
      <el-input
          type="textarea"
          :rows="3"
          placeholder="输入消息..."
          v-model="inputMessage"
          @keyup.enter.native="sendMessage"
          resize="none"
      ></el-input>
      <div class="action-buttons">
        <el-button type="primary" @click="sendMessage">发送</el-button>
        <el-button @click="clearInput">清空</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SellerChat',
  data() {
    return {
      seller: {
        id: '',
        name: '卖家名称',
        avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
        productCount: 0
      },
      inputMessage: '',
      messages: [
        {
          sender: 'seller',
          content: '您好，您对我的商品有什么问题吗？',
          time: new Date(Date.now() - 3600000)
        }
      ]
    };
  },
  created() {
    this.fetchSellerInfo();
  },
  mounted() {
    this.scrollToBottom();
  },
  methods: {
    fetchSellerInfo() {
      // 从路由参数获取卖家ID
      const sellerId = this.$route.query.SellerId||'';

      if (!sellerId) {
        this.$message.error('缺少卖家信息');
        this.$router.back();
        return;
      }

      // 这里应该是API调用获取卖家信息
      // 模拟数据
      this.seller = {
        id: sellerId,
        name: '卖家' + sellerId.slice(0, 4),
        avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
        productCount: 12
      };
    },
    sendMessage() {
      if (!this.inputMessage.trim()) return;

      const newMessage = {
        sender: 'me',
        content: this.inputMessage,
        time: new Date()
      };

      this.messages.push(newMessage);
      this.inputMessage = '';

      // 模拟卖家回复
      setTimeout(() => {
        this.messages.push({
          sender: 'seller',
          content: '感谢您的咨询，我会尽快回复您的问题',
          time: new Date()
        });
        this.scrollToBottom();
      }, 1500);

      this.scrollToBottom();
    },
    clearInput() {
      this.inputMessage = '';
    },
    backToProduct() {
      this.$router.back();
    },
    formatTime(time) {
      return time.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$refs.messageArea;
        container.scrollTop = container.scrollHeight;
      });
    }
  }
};
</script>

<style scoped lang="scss">
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f5f5;
}

.seller-info {
  display: flex;
  align-items: center;
  padding: 15px;
  background-color: white;
  border-bottom: 1px solid #eaeaea;

  .seller-details {
    flex: 1;
    margin-left: 15px;

    h3 {
      margin: 0;
      font-size: 18px;
    }

    p {
      margin: 5px 0 0;
      font-size: 14px;
      color: #999;
    }
  }
}

.message-area {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #e5ddd5;
  background-image: url('https://web.whatsapp.com/img/bg-chat-tile-light_a4be512e7195b6b733d9110b408f075d.png');
}

.message-bubble {
  margin-bottom: 15px;
  display: flex;

  &.sent {
    justify-content: flex-end;

    .message-content {
      background-color: #dcf8c6;
      border-radius: 7.5px 0 7.5px 7.5px;
    }
  }

  &.received {
    justify-content: flex-start;

    .message-content {
      background-color: white;
      border-radius: 0 7.5px 7.5px 7.5px;
    }
  }

  .message-content {
    max-width: 70%;
    padding: 8px 12px;
    box-shadow: 0 1px 0.5px rgba(0, 0, 0, 0.13);

    p {
      margin: 0;
      word-wrap: break-word;
    }

    .message-time {
      display: block;
      font-size: 11px;
      color: #999;
      margin-top: 5px;
      text-align: right;
    }
  }
}

.input-area {
  padding: 10px;
  background-color: white;
  border-top: 1px solid #eaeaea;

  .action-buttons {
    margin-top: 10px;
    text-align: right;
  }
}
</style>