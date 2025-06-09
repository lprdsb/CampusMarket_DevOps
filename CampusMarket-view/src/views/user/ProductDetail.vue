<template>
  <div class="detail-container">
    <section class="info-section">
      <div class="info-section-up">
        <div class="info-left">
          <div class="name-container">
            <h1 class="product-name">
              {{ product.name }}
            </h1>
            <button @click="saveOperation" class="actions btn btn-star">
              <i :class="saveFlag ? 'el-icon-star-on' : 'el-icon-star-off'"></i>
            </button>
          </div>
          <div class="price-category">
            <span class="price">￥{{ product.price.toFixed(2) }}</span>
            <span class="category">{{ product.categoryName }}</span>
          </div>
          <div class="seller-info" @click="handleRouteSelect('/space?userId=' + product.userId)">
            <img :src="product.userAvatar" alt="avatar" class="seller-avatar" />
            <span class="seller-name">{{ product.userName }}</span>
          </div>
          <div class="stock-level">
            <span>{{ product.saveNumber }}人收藏</span>
            <span>{{ product.viewNumber }}人浏览</span>
            <!-- <span>{{ product.oldLevel }}成新</span> -->
            <span>库存: {{ product.inventory }}</span>
          </div>
          <h2 class="product-name">
            商品详情
          </h2>
          <span v-html="product.detail"></span>
        </div>
        <div class="cover-section">
          <div class="cover-display" v-if="coverItem">
            <i @click="coverToLeft" class="el-icon-arrow-left"></i>
            <img :src="coverItem" alt="" />
            <i @click="coverToRight" class="el-icon-arrow-right"></i>
          </div>
          <div class="cover-preview">
            <div class="cover-thumb" v-for="(coverItem, index) in coverList" :key="index"
                 :class="{ active: coverIndex === index }" @click="coverSelected(coverItem, index)">
              <img :src="coverItem" alt="" />
            </div>
          </div>
        </div>
      </div>

      <div class="info-right">
        <div class="actions">
          <button @click="likeProduct" class="btn btn-primary">我想要</button>
          <button @click="goToChat" class="btn btn-secondary">和买家聊天</button>
          <button @click="buyProduct" class="btn btn-success">立即购买</button>
          <!-- <p>{{ this.saveFlag }}</p> -->
          <button @click="submitComplaint" class="btn btn-complaint">投诉</button>
        </div>
        <div v-if="userInfo !== null" class="evaluation-wrapper">
          <Evaluations contentType="PRODUCT" :contentId="productId" />
        </div>
      </div>
    </section>

    <el-dialog :show-close="false" :visible.sync="dialogProductOperaion" width="50%" class="order-dialog">
      <div class="order-container">
        <div class="header">
          <h2>确认下单</h2>
          <p class="sub-text">请核对商品信息后填写下单详情</p>
        </div>

        <div class="product-info-grid">
          <img :src="product.userAvatar" class="avatar" />
          <div class="info">
            <h3 class="product-name">{{ product.name }}</h3>
            <p class="price"><span class="symbol">￥</span>{{ product.price }}</p>
            <p class="meta">{{ product.categoryName }} · {{ product.oldLevel }}成新</p>
            <p class="meta">库存：{{ product.inventory }}</p>
            <p class="owner">卖家：{{ product.userName }}</p>
          </div>
        </div>

        <el-divider></el-divider>

        <div class="form-section">
          <el-form label-position="top">
            <el-form-item label="购买数量">
            </el-form-item>
              <el-input-number v-model="buyNumber" :min="1" :max="product.inventory" />
              <el-form-item label="支付方式">
                <el-radio-group v-model="paymentMethod" @change="showPaymentSelection">
                  <el-radio label="online">
                    线上支付
                    <transition name="el-zoom-in-center">
                      <i v-if="paymentMethod === 'online'" class="el-icon-check check-icon"></i>
                    </transition>
                  </el-radio>
                  <el-radio label="offline">
                    线下支付
                    <transition name="el-zoom-in-center">
                      <i v-if="paymentMethod === 'offline'" class="el-icon-check check-icon"></i>
                    </transition>
                  </el-radio>
                </el-radio-group>
              </el-form-item>

            <el-form-item label="备注信息">
              <el-input type="textarea" :rows="4" v-model="detail" placeholder="填写备注（选填）" />
            </el-form-item>
          </el-form>
        </div>
      </div>

      <span slot="footer" class="footer-actions">
        <button class="btn btn-secondary" @click="cannelBuy()">取消</button>
        <button class="btn btn-primary" @click="buyConfirm()">下单</button>
      </span>
    </el-dialog>
    <el-dialog :show-close="false" :visible.sync="showComplaint" class="complaint-dialog">

      <div class="complaint-container">
        <div class="header">
          <h2>投诉</h2>
        </div>

        <div class="form-section">
          <el-form label-position="top">
            <el-form-item label="投诉内容">
              <el-input type="textarea" :rows="4" v-model="content" placeholder="填写投诉内容" />
            </el-form-item>
          </el-form>
        </div>
      </div>

      <span slot="footer" class="footer-actions">
        <button class="btn btn-primary" @click="submit()">提交</button>
        <button class="btn btn-secondary" @click="cannelComplaint()">取消</button>
      </span>
    </el-dialog>
    <!-- <el-dialog :show-close="false" :visible.sync="dialogProductOperaion" width="62%">
      <div class="dialog-content">
        <p>商品下单</p>
        <div class="info">
          <div class="decimal">
            <span class="price"><span class="symbol">￥</span>{{ product.price }}</span>
            <span class="dot"></span>
            <span>{{ product.categoryName }}</span>
            <span class="dot"></span>
            <img class="small-avatar" :src="product.userAvatar" alt="" />
            <span>{{ product.userName }}</span>
          </div>
          <div class="decimal">
            <span class="dot"></span>
            <span>{{ product.oldLevel }}成新</span>
            <span class="dot"></span>
            <span>库存&nbsp;{{ product.inventory }}</span>
          </div>
          <div class="name">{{ product.name }}</div>
        </div>
        <div>
          <p>下单数量</p>
          <el-input-number v-model="buyNumber" :min="1" :max="product.inventory" label="请选择"></el-input-number>
        </div>
        <div>
          <p>备注信息</p>
          <el-input type="textarea" :rows="3" v-model="detail"></el-input>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <span class="cancel-button" @click="cannelBuy()">取消下单</span>
        <span class="confirm-button" @click="buyConfirm()">确定下单</span>
      </span>
    </el-dialog> -->
  </div>
</template>
<script>
import { getUserInfo } from "@/utils/storage"
import Evaluations from "@/components/Evaluations"
export default {
  components: { Evaluations },
  name: 'ProductDetail',
  data() {
    return {
      productId: null,
      product: {},
      coverList: [],
      coverIndex: 0,
      coverItem: null,
      keyInterval: null,
      saveFlag: false, // 判断用户是否已经收藏
      dialogProductOperaion: false,
      buyNumber: 1,
      detail: '',
      userInfo: null,
      content: '',
      showComplaint: false,
      showCheckIcon: false,
      paymentMethod: '',
    }
  },
  created() {
    this.getParam();
    this.viewOperation();
    // console.log("asdasd" + this.productId);
  },
  beforeDestroy() {
    this.clearBanner(); // 清除定时器
  },
  methods: {
    showPaymentSelection() {
      // 等待视图更新后再设置 showCheckIcon
      this.$nextTick(() => {
        this.showCheckIcon = true;

        // 3秒后自动隐藏勾选提示
        setTimeout(() => {
          this.showCheckIcon = false;
        }, 5000);
      });
    },
    cannelComplaint() {
      this.showComplaint = false;
    },
    submitComplaint() {
      this.showComplaint = true;
    },
    async submit() {
      const userInfo = getUserInfo();
      // console.log(userInfo);
      await this.$axios.post('/api/complaint/submit', {
        complainantId: userInfo.id,
        productId: this.productId,
        content: this.content
      });
      this.$emit('update:visible', false);
      this.$message.success('投诉已提交');
      this.cannelComplaint();
    },
    // 浏览操作
    viewOperation() {
      const userInfo = getUserInfo();
      if (userInfo === null) { // 没登录不用记录
        return;
      }
      this.userInfo = userInfo;
      // 对于用户这是无感的
      this.$axios.post(`/interaction/view/${this.productId}`).then(res => {
        const { data } = res; // 解构
        if (data.code === 200) {
          console.log("用户浏览已经处理");
        }
      }).catch(error => {
        console.log("浏览记录异常：", error);
      })
    },
    handleRouteSelect(path) {
      // console.log(path);
      if (this.$router.currentRoute.fullPath !== path) {
        this.$router.push(path);
      }
    },
    /**
     * 商品下单
     */
    buyConfirm() {
      const ordersDTO = {
        productId: this.product.id,
        buyNumber: this.buyNumber,
        detail: this.detail
      }
      this.$axios.post(`/product/buyProduct`, ordersDTO).then(res => {
        const { data } = res; // 解构
        if (data.code === 200) {
          this.$notify({
            duration: 1000,
            title: '下单操作',
            message: data.msg,
            type: 'success'
          });
          this.fetchProduct(this.product.id);
          this.cannelBuy();
        } else {
          this.$notify({
            duration: 2000,
            title: '下单操作',
            message: data.msg,
            type: 'error'
          });
        }
      }).catch(error => {
        this.$notify({
          duration: 2000,
          title: '下单操作',
          message: error,
          type: 'error'
        });
        console.log("商品下单异常：", error);
      })
    },
    cannelBuy() {
      this.dialogProductOperaion = false;
      this.buyNumber = 1;
    },
    buyProduct() {
      const userInfo = getUserInfo();
      if (userInfo === null) { // 没登录不用记录
        this.$notify({
          duration: 1000,
          title: '未登录',
          message: '登录后才可操作',
          type: 'info'
        });
        this.$router.push('/login');
        return;
      }
      this.dialogProductOperaion = true;
    },
    likeProduct() {
      this.$axios.post(`/interaction/likeProduct/${this.product.id}`).then(res => {
        const { data } = res; // 解构
        if (data.code === 200) {
          this.$notify({
            duration: 1000,
            title: '想要操作通知',
            message: data.msg,
            type: 'success'
          });
        } else {
          this.$notify({
            duration: 2000,
            title: '想要操作通知',
            message: data.msg,
            type: 'info'
          });
        }
      }).catch(error => {
        console.log("商品---想要---异常：", error);
      })
    },
    goToChat() {
      // 假设聊天页面的路由路径是 '/chat'
      this.$router.push({ path: '/chat', query: { ReceiverId: this.product.userId } });

      // 或者直接跳转到指定买家的聊天窗口（带参数）
      // this.$router.push({ path: '/chat', query: { buyerId: '123' } });
    },
    querySaveStatus() {
      // 判断用户是否已经登录
      const userInfo = getUserInfo();
      if (userInfo === null) { // 没登录不用查
        console.log("用户未登录");
        return;
      }
      const interactionQueryDto = {
        userId: userInfo.id,
        productId: this.product.id,
        type: 1 // 1代表的是收藏行为
      };
      this.$axios.post('/interaction/query', interactionQueryDto).then(res => {
        const { data } = res; // 解构
        if (data.code === 200) {
          // 代表没有收藏
          this.saveFlag = data.total !== 0;
        }
      }).catch(error => {
        console.log("商品查询异常：", error);
      })
    },
    /**
     * 收藏操作 （收藏跟取消收藏是一组对立的操作）
     */
    saveOperation() {
      const userInfo = getUserInfo();
      if (userInfo === null) { // 没登录不用记录
        this.$notify({
          duration: 1000,
          title: '未登录',
          message: '登录后才可操作',
          type: 'info'
        });
        this.$router.push('/login');
        return;
      }
      this.$axios.post(`/interaction/saveOperation/${this.product.id}`).then(res => {
        const { data } = res; // 解构
        if (data.code === 200) {
          // 代表没有收藏
          this.saveFlag = data.data;
          this.$notify({
            duration: 1000,
            title: '收藏操作成功',
            message: data.msg,
            type: 'success'
          });
        }
      }).catch(error => {
        console.log("商品查询异常：", error);
      })
    },
    clearBanner() {
      if (this.keyInterval) {
        clearInterval(this.keyInterval);
        this.keyInterval = null; // 重置定时器引用
      }
    },
    startBanner() {
      this.keyInterval = setInterval(() => {
        if (this.coverIndex === this.coverList.length - 1) {
          this.coverIndex = 0;
        } else {
          this.coverIndex = this.coverIndex + 1;
        }
        this.coverItem = this.coverList[this.coverIndex];
      }, 5000);
    },
    coverToLeft() {
      if (this.coverIndex === 0) {
        this.coverIndex = this.coverList.length - 1;
      } else {
        this.coverIndex = this.coverIndex - 1;
      }
      this.coverItem = this.coverList[this.coverIndex];
    },
    coverToRight() {
      if (this.coverIndex === this.coverList.length - 1) {
        this.coverIndex = 0;
      } else {
        this.coverIndex = this.coverIndex + 1;
      }
      this.coverItem = this.coverList[this.coverIndex];
    },
    coverSelected(coverItem, index) {
      this.coverItem = coverItem;
      this.coverIndex = index;
    },
    /**
     * 从路径上取得商品ID
     */
    getParam() {
      const param = this.$route.query;
      this.productId = Number(param.productId);
      this.fetchProduct(this.productId);
    },
    coverListParse(product) {
      if (product.coverList === null) {
        return;
      }
      this.coverList = product.coverList.split(',');
      // 默认选中第一张封面
      this.coverItem = this.coverList[0];
      // 启动定时器，定时轮播
      this.startBanner();
    },
    fetchProduct(productId) {
      this.$axios.post('/product/query', { id: productId }).then(res => {
        const { data } = res; // 解构
        if (data.code === 200) {
          this.product = data.data[0];
          this.coverListParse(this.product);
          this.querySaveStatus();
        }
      }).catch(error => {
        console.log("商品查询异常：", error);
      })
    },
  }
};
</script>
<style scoped lang="scss">
.order-dialog::v-deep(.el-dialog) {
  border-radius: 16px;
}

.order-container {
  padding: 20px;
  // background-color: #f9fbff;
  border-radius: 12px;
}

.complaint-container {
  padding: 20px;
  // background-color: #f9fbff;
  border-radius: 12px;
}

.header {
  text-align: center;

  h2 {
    margin: 0;
    font-size: 22px;
    font-weight: 600;
    color: #1e2f50;
  }

  .sub-text {
    font-size: 13px;
    color: #8a9ab0;
    margin-top: 4px;
  }
}

.product-info-grid {
  display: grid;
  grid-template-columns: 80px 1fr;
  gap: 16px;
  align-items: center;
  margin: 20px 0;

  .avatar {
    width: 80px;
    height: 80px;
    border-radius: 12px;
    object-fit: cover;
    border: 2px solid #cce0ff;
  }

  .info {
    display: flex;
    flex-direction: column;
    gap: 4px;
    width: 90%;

    .product-name {
      font-size: 18px;
      font-weight: 600;
      color: #333;
    }

    .price {
      font-size: 22px;
      font-weight: bold;
      color: #007bff;

      .symbol {
        font-size: 16px;
      }
    }

    .meta {
      font-size: 13px;
      color: #6c7b91;
    }

    .owner {
      font-size: 13px;
      color: #444;
    }
  }
}

.form-section {
  margin-top: 20px;

  ::v-deep(.el-form-item__label) {
    font-weight: 500;
    color: #444;
    margin-bottom: 6px;
  }

  ::v-deep(.el-input__inner),
  ::v-deep(.el-textarea__inner) {
    border-radius: 8px;
  }
}

.footer-actions {
  padding: 10px 24px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;

  .btn {
    padding: 14px 14px;
    border: none;
    border-radius: 30px;
    font-size: 18px;
    font-weight: 600;
    cursor: pointer;
    box-shadow: 0 4px 10px rgb(0 123 255 / 0.4);
    transition: all 0.3s ease;

    &.btn-primary {
      background: linear-gradient(45deg, #2ecc71, #27ae60);
      color: white;

      &:hover {
        background: linear-gradient(45deg, #27ae60, #2ecc71);
        box-shadow: 0 6px 14px rgb(39 174 96 / 0.7);
      }
    }

    &.btn-secondary {
      background: linear-gradient(45deg, #e26139, #df2605);
      color: white;

      &:hover {
        background: linear-gradient(45deg, #df2605, #e26139);
        box-shadow: 0 6px 14px rgb(41 128 185 / 0.7);
      }
    }

    &.btn-star {
      background: #fff;
      color: #007bff;
      border: 2px solid #007bff;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 6px;
      font-size: 16px;

      i {
        font-size: 20px;
        color: #ffcc00;
      }

      &:hover {
        background: #007bff;
        color: white;

        i {
          color: white;
        }
      }
    }
  }
}

.detail-container {
  display: flex;
  gap: 40px;
  padding: 24px;
  background-color: #EFF6FF;
  justify-content: center;
}

.info-section {
  display: flex;
  flex-direction: column;
  gap: 48px;
  background: white;
  border-radius: 20px;
  padding: 32px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  width: 50%;

  .info-section-up {
    display: flex;
    flex-direction: row;

  }

  .info-left {
    flex: 1.2;

    .name-container {
      gap: 10px;
      display: flex;
      flex-direction: row;

      .product-name {
        font-size: 32px;
        font-weight: 700;
        margin-bottom: 16px;
        color: #007bff;
      }

      .btn {
        margin-top: 25px;
        display: flex;
        flex-direction: row;
        justify-content: center;
        padding: 14px 14px;
        border: none;
        border-radius: 30px;
        font-size: 18px;
        font-weight: 600;
        cursor: pointer;
        box-shadow: 0 4px 10px rgb(0 123 255 / 0.4);
        transition: all 0.3s ease;

        &.btn-star {
          background: #fff;
          color: #007bff;
          border: 2px solid #007bff;
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 6px;
          font-size: 16px;
          height: 50px;

          i {
            font-size: 20px;
            color: #ffcc00;
          }

          &:hover {
            background: #007bff;
            color: white;

            i {
              color: white;
            }
          }
        }
      }
    }

    .price-category {
      display: flex;
      gap: 24px;
      font-size: 20px;
      margin-bottom: 18px;

      .price {
        font-weight: 700;
        color: #e74c3c;
      }

      .category {
        color: #555;
        font-style: italic;
      }
    }

    .seller-info {
      display: flex;
      align-items: center;
      gap: 14px;
      cursor: pointer;
      margin-bottom: 16px;

      .seller-avatar {
        width: 44px;
        height: 44px;
        border-radius: 50%;
        box-shadow: 0 0 8px rgba(0, 123, 255, 0.5);
        object-fit: cover;
        transition: transform 0.3s ease;
      }

      &:hover .seller-avatar {
        transform: scale(1.1);
      }

      .seller-name {
        font-weight: 600;
        color: #007bff;
      }
    }

    .stock-level {
      font-size: 16px;
      color: #666;
      margin-bottom: 24px;
      display: flex;
      gap: 16px;
    }

    .product-detail {
      font-size: 16px;
      line-height: 1.6;
      color: #444;
      max-height: 180px;
      overflow-y: auto;
      padding-right: 12px;
      border-right: 2px solid #007bff1a;
    }
  }

  .info-right {
    flex: 0.8;
    display: flex;
    flex-direction: column;
    justify-content: space-between;

    .stats {
      font-size: 14px;
      color: #888;
      display: flex;
      gap: 20px;
      margin-bottom: 24px;
      justify-content: center;
    }

    .actions {
      display: flex;
      flex-direction: row;
      gap: 16px;
      justify-content: center;

      .btn {
        padding: 14px 14px;
        border: none;
        border-radius: 30px;
        font-size: 18px;
        font-weight: 600;
        cursor: pointer;
        box-shadow: 0 4px 10px rgb(0 123 255 / 0.4);
        transition: all 0.3s ease;

        &.btn-complaint {
          background: linear-gradient(45deg, #e26139, #df2605);
          color: white;

          &:hover {
            background: linear-gradient(45deg, #df2605, #e26139);
            box-shadow: 0 6px 14px rgb(41 128 185 / 0.7);
          }
        }

        &.btn-primary {
          background: linear-gradient(45deg, #2ecc71, #27ae60);
          color: white;

          &:hover {
            background: linear-gradient(45deg, #27ae60, #2ecc71);
            box-shadow: 0 6px 14px rgb(39 174 96 / 0.7);
          }
        }

        &.btn-secondary {
          background: linear-gradient(45deg, #3498db, #2980b9);
          color: white;

          &:hover {
            background: linear-gradient(45deg, #2980b9, #3498db);
            box-shadow: 0 6px 14px rgb(41 128 185 / 0.7);
          }
        }

        &.btn-success {
          background: linear-gradient(45deg, #9b59b6, #8e44ad);
          color: white;

          &:hover {
            background: linear-gradient(45deg, #8e44ad, #9b59b6);
            box-shadow: 0 6px 14px rgb(142 68 173 / 0.7);
          }
        }



        &.btn-star {
          background: #fff;
          color: #007bff;
          border: 2px solid #007bff;
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 6px;
          font-size: 16px;

          i {
            font-size: 20px;
            color: #ffcc00;
          }

          &:hover {
            background: #007bff;
            color: white;

            i {
              color: white;
            }
          }
        }
      }
    }

    .evaluation-wrapper {
      margin-top: 30px;
      background: #f0f6ff;
      padding: 16px;
      border-radius: 14px;
      box-shadow: inset 0 0 12px #d0e4ff;
      flex-grow: 1;
      overflow-y: auto;
    }
  }
}

.cover-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #ffffff;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 120, 255, 0.15);
  height: 450px;

  .cover-display {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-top: 16px;

    img {
      width: 300px;
      height: 300px;
      border-radius: 12px;
      border: 2px solid #cce6ff;
    }

    i {
      font-size: 24px;
      cursor: pointer;
      padding: 10px;
      border-radius: 50%;
      color: #007bff;
      transition: background 0.3s;

      &:hover {
        background-color: #e6f0ff;
      }
    }
  }

  .cover-preview {
    display: flex;
    gap: 12px;
    flex-wrap: wrap;

    .cover-thumb {
      border: 2px solid transparent;
      border-radius: 10px;
      cursor: pointer;
      padding: 4px;
      transition: border-color 0.3s;

      img {
        width: 80px;
        height: 80px;
        border-radius: 8px;
      }

      &.active {
        border-color: #007bff;
      }
    }
  }
}

.info-container {
  display: flex;
  flex-direction: column;
  gap: 100px;
}

.info {
  width: 1500px;
  // height: 300px;
  background: #ffffff;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 120, 255, 0.1);

  .decimal {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 10px;
    font-size: 14px;
    margin-bottom: 10px;

    .price {
      font-size: 28px;
      font-weight: bold;
      color: #007bff;

      .symbol {
        font-size: 18px;
      }
    }

    .owner img {
      width: 20px;
      height: 20px;
      border-radius: 50%;
      margin-right: 4px;
    }

    .bargain {
      background: #e6f0ff;
      color: #0056b3;
      padding: 2px 6px;
      border-radius: 4px;
      font-size: 12px;
    }
  }

  .dot {
    width: 6px;
    height: 6px;
    background: #b0d4ff;
    border-radius: 50%;
  }

  .love {
    color: #888;
  }

  .name {
    font-size: 22px;
    font-weight: 600;
    margin: 12px 0;
  }

  .operation {
    display: flex;
    justify-content: space-between;
    margin-top: 16px;

    .left,
    .right {
      display: flex;
      align-items: center;
      gap: 12px;
    }

    span {
      padding: 6px 12px;
      border-radius: 20px;
      background: #e6f0ff;
      cursor: pointer;
      transition: background 0.3s;
    }

    span:hover {
      background: #b3daff;
    }
  }
}

.dialog-content {
  padding: 20px;
}

.small-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
  margin: 0 6px;
  /* 和文字间距调整 */
  vertical-align: middle;
  /* 让头像和文字垂直对齐 */
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  padding: 12px 20px;

  .cancel-button,
  .confirm-button {
    padding: 8px 20px;
    border-radius: 20px;
    cursor: pointer;
  }

  .cancel-button {
    background: #f0f0f0;
  }

  .confirm-button {
    background: #007bff;
    color: white;
  }
}
.check-icon {
  margin-left: 8px;
  color: #67C23A;
  font-size: 16px;
  vertical-align: middle;
}

.el-radio {
  position: relative;
  padding-right: 25px; // 为勾选图标留出空间

  &__label {
    display: inline-flex;
    align-items: center;
  }
}

// 添加选中状态的动画效果
@keyframes checkBounce {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.3); }
}

.el-icon-check {
  animation: checkBounce 0.5s ease;
}
.check-icon {
  margin-left: 8px;
  color: #67C23A;
  font-size: 16px;
  vertical-align: middle;
}

.el-radio {
  position: relative;
  padding-right: 25px; // 为勾选图标留出空间

  &__label {
    display: inline-flex;
    align-items: center;
  }
}

// 添加选中状态的动画效果
@keyframes checkBounce {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.3); }
}

.el-icon-check {
  animation: checkBounce 0.5s ease;
}
</style>