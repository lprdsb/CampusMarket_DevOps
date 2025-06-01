<template>
  <div class="orders-page">
    <div class="orders-left">
      <div class="condition">
        <span class="trade">
          <span :class="{ active: tradeStatusSelectedItem.name === tradeStatus.name }"
            @click="tradeStatusSelected(tradeStatus)" v-for="(tradeStatus, index) in tradeStatusList" :key="index">{{
              tradeStatus.name }}</span>
        </span>
        <el-input size="small" style="width: 180px;" v-model="ordersQueryDto.code" placeholder="订单号" clearable
          @clear="handleFilterClear">
          <el-button slot="append" @click="fetchOrders" icon="el-icon-search"></el-button>
        </el-input>
      </div>


      <!-- <el-input size="small" v-model="ordersQueryDto.code" placeholder="订单号" clearable @clear="handleFilterClear">
        <el-button slot="append" @click="fetchOrders" icon="el-icon-search"></el-button>
      </el-input> -->

      <div v-if="ordersList.length === 0" class="empty-container">
        <el-empty description="未找到符合条件的订单数据"></el-empty>
      </div>

      <div v-else class="orders-list">
        <div class="item-order" v-for="(orderInfo, index) in ordersList" :key="index" @click="selectOrder(orderInfo)">
          <div class="img-wrap">
            <img :src="orderInfo.cover" alt="" />
          </div>
          <div class="info">
            <div class="title">{{ orderInfo.productTitle }}</div>
            <div class="price">￥{{ totalPrice(orderInfo) }}</div>
          </div>
        </div>
      </div>
    </div>

    <div class="orders-right" v-if="selectedOrder">
      <div class="order-detail">
        <h2>订单详情</h2>
        <p><strong>订单号：</strong>{{ selectedOrder.code }}</p>
        <p><strong>商品：</strong>{{ selectedOrder.productTitle }}</p>
        <p><strong>数量：</strong>{{ selectedOrder.buyNumber }}</p>
        <p><strong>价格：</strong>￥{{ totalPrice(selectedOrder) }}</p>
        <p><strong>备注：</strong>{{ selectedOrder.detail }}</p>
        <p v-if="selectedOrder.refundStatus !== null">
          <strong>退款状态：</strong>
          <span class="confirmed">
            {{ selectedOrder.isRefundConfirm ? '钱款已原路返回' : '申请退款中，待卖家确认...' }}
          </span>
        </p>
        <div class="actions">
          <span v-if="!selectedOrder.isRefundConfirm">
            <el-button type="primary" v-if="!selectedOrder.tradeStatus"
              @click="placeAnOrder(selectedOrder)">下单</el-button>
            <el-button type="warning" v-else @click="refund(selectedOrder)">申请退款</el-button>
          </span>
          <el-button type="danger" v-if="selectedOrder.tradeStatus" @click="del(selectedOrder)">删除</el-button>
          <!-- <el-button type="danger">下单</el-button> -->
          <el-button @click="submitComplaint">投诉</el-button>
          <!-- <ComplaintForm :visible.sync="showComplaint" :targetId="selectedOrder.sellerId" :orderId="selectedOrder.id"
            :complainantId="userId" /> -->
        </div>
      </div>
    </div>


    <el-dialog :show-close="false" :visible.sync="showComplaint" class="complaint-dialog">

      <div class="complaint-container">
        <div class="header">
          <h2>投诉</h2>
          <!-- <p class="sub-text">请核对商品信息后填写下单详情</p> -->
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
      <!-- <el-form @submit.native.prevent="submit">
        <el-form-item label="投诉内容">
          <el-input type="textarea" v-model="content" required />
        </el-form-item>
        <el-button type="primary" @click="submit">提交</el-button>
        <el-button type="primary" @click="cannelComplaint()">取消</el-button>
      </el-form> -->
    </el-dialog>
  </div>
</template>
<script>
import ComplaintForm from '@/components/ComplaintForm.vue';
export default {
  name: 'BuyOrders',
  components: { ComplaintForm },
  data() {
    return {
      selectedOrder: null,
      ordersList: [],
      ordersQueryDto: {},
      tradeStatusSelectedItem: {},
      tradeStatusList: [{ tradeStatus: null, name: '全部' }, { tradeStatus: true, name: '已支付' }, { tradeStatus: false, name: '未支付' }],
      showComplaint: false,
      userId: '', // 假设用户ID在此获取，实际情况请根据项目调整,
      content: ''
    }
  },
  created() {
    this.auth();
  },
  methods: {

    cannelComplaint() {
      this.showComplaint = false;
    },
    submitComplaint() {
      this.showComplaint = true;
    },
    auth() {
      this.$axios.get('/user/auth').then(res => {
        const { data } = res;
        if (data.code === 200) {
          this.selfInfo = data.data;
          // this.fetchUser();
          this.userId = this.selfInfo.id;
          this.fetchOrders();
          this.tradeStatusSelected(this.tradeStatusList[0]);
          // console.log(this.userId + "aasdasdasd");
        }
      }).catch(error => {
        this.$notify.error({
          title: '查询异常',
          message: error
        });
      });
    },
    async submit() {
      console.log(this.selectedOrder);
      await this.$axios.post('/api/complaint/submit', {
        complainantId: this.userId,
        targetId: this.selectedOrder.sellerId,
        orderId: this.selectedOrder.id,
        content: this.content
      });
      this.$emit('update:visible', false);
      this.$message.success('投诉已提交');
      this.cannelComplaint();
    },
    tradeStatusSelected(tradeStatusItem) {
      this.tradeStatusSelectedItem = tradeStatusItem;
      this.ordersQueryDto.tradeStatus = tradeStatusItem.tradeStatus;
      this.fetchOrders();
    },
    selectOrder(order) {
      this.selectedOrder = order;
    },
    handleFilterClear() {
      this.ordersQueryDto.code = '';
      this.fetchOrders();
    },
    async del(orderInfo) {
      const confirmed = await this.$swalConfirm({
        title: '删除订单数据',
        text: `删除后不可恢复，是否继续？`,
        icon: 'warning',
      });
      if (confirmed) {
        try {
          let ids = [orderInfo.id];
          const response = await this.$axios.post(`/orders/batchDelete`, ids);
          if (response.data.code === 200) {
            this.$notify({
              duration: 1000,
              title: '信息删除',
              message: '删除成功',
              type: 'success'
            });
            this.fetchOrders();
            return;
          }
        } catch (error) {
          this.$message.error("订单信息删除异常：", error);
          console.error(`订单信息删除异常：`, error);
        }
      }
    },
    /**
     * 计算总金额
     */
    totalPrice(orderInfo) {
      const totalPrice = orderInfo.buyNumber * orderInfo.buyPrice;
      // 保留两位小数点
      return parseFloat(totalPrice).toFixed(2);
    },
    refund(orders) {
      this.$axios.post(`/product/refund/${orders.id}`).then(res => {
        const { data } = res; // 解构
        if (data.code === 200) {
          this.$notify({
            duration: 1000,
            title: '退款操作',
            message: '退款成功',
            type: 'success'
          });
          this.fetchOrders();
        }
      }).catch(error => {
        console.log("退款异常：", error);
      })
    },
    placeAnOrder(orders) {
      this.$axios.post(`/product/placeAnOrder/${orders.id}`).then(res => {
        const { data } = res; // 解构
        if (data.code === 200) {
          this.$notify({
            duration: 1000,
            title: '下单',
            message: '下单成功',
            type: 'success'
          });
          this.fetchOrders();
        }
      }).catch(error => {
        console.log("下单异常：", error);
      })
    },
    coverParse(coverList) {
      if (coverList.productCover === null) {
        return;
      }
      return coverList.productCover.split(',')[0];
    },
    /**
     * 购物订单
     */
    fetchOrders() {
      // console.log(this.userId + "aasdasdasd");
      this.$axios.post('/orders/queryUser', this.ordersQueryDto).then(res => {
        const { data } = res; // 解构
        if (data.code === 200) {
          this.ordersList = data.data.map(order => {
            return {
              id: order.id,
              code: order.code,
              productTitle: order.productTitle,
              detail: order.detail,
              buyPrice: order.buyPrice,
              buyNumber: order.buyNumber,
              tradeStatus: order.tradeStatus,
              tradeTime: order.tradeTime,
              refundStatus: order.refundStatus,
              refundTime: order.refundTime,
              isRefundConfirm: order.isRefundConfirm,
              createTime: order.createTime,
              tradeTime: order.tradeTime,
              refundTime: order.refundTime,
              cover: this.coverParse(order)
            }
          });
        }
      }).catch(error => {
        console.log("订单数据查询异常：", error);
      })
    },
  }
};
</script>
<style scoped lang="scss">
.complaint-dialog::v-deep(.el-dialog) {
  border-radius: 16px;
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

.complaint-container {
  padding: 20px;
  // background-color: #f9fbff;
  border-radius: 12px;
}

.orders-page {
  display: flex;
  height: calc(100vh - 40px);
  gap: 20px;
}

.el-button {
  font-size: 20px;
  // padding: 4px 14px;
  border-radius: 6px;
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

.orders-left {
  width: 40%;
  padding: 20px;
  background: #f0f6ff;
  overflow-y: auto;
  border-right: 2px solid #cdddfb;

  .condition {
    margin-bottom: 20px;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .trade {
      // background: #dceeff;
      padding: 6px 10px;
      border-radius: 6px;

      span {
        margin-right: 8px;
        padding: 6px 12px;
        border-radius: 4px;
        cursor: pointer;
        background: #fff;
        color: #1d61c7;

        &.active {
          background: #1d61c7;
          color: white;
        }
      }
    }
  }

  .orders-list {
    display: flex;
    flex-direction: column;
    gap: 12px;

    .item-order {
      display: flex;
      align-items: center;
      background: white;
      padding: 10px;
      border-radius: 8px;
      cursor: pointer;
      transition: all 0.2s;

      &:hover {
        background: #e7f1ff;
      }

      .img-wrap {
        width: 60px;
        height: 60px;
        border-radius: 6px;
        overflow: hidden;
        margin-right: 12px;

        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
      }

      .info {
        .title {
          font-size: 14px;
          font-weight: bold;
          color: #1a3a8f;
        }

        .price {
          color: #409eff;
          font-size: 13px;
        }
      }
    }
  }
}

.orders-right {
  flex: 1;
  padding: 30px;
  // background: #eaf1fb;
  overflow-y: auto;

  .order-detail {
    background: white;
    padding: 20px;
    border-radius: 12px;
    box-shadow: 0 0 10px rgba(0, 123, 255, 0.1);

    h2 {
      color: #1a3a8f;
      margin-bottom: 20px;
    }

    p {
      margin-bottom: 10px;
      font-size: 14px;
      color: #333;

      .confirmed {
        color: #2b8a3e;
      }
    }

    .actions {
      margin-top: 20px;

      .el-button {
        margin-right: 10px;
      }
    }
  }
}

.empty-container {
  padding: 30px;
  text-align: center;
}
</style>
