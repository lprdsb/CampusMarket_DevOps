<template>
    <div>
      <div class="condition">
        <span class="trade">
          <span
            :class="{ active: tradeStatusSelectedItem.name === tradeStatus.name }"
            @click="tradeStatusSelected(tradeStatus)"
            v-for="(tradeStatus, index) in tradeStatusList"
            :key="index"
            >{{ tradeStatus.name }}</span
          >
        </span>
        <el-input
          size="small"
          style="width: 180px;"
          v-model="ordersQueryDto.code"
          placeholder="订单号"
          clearable
          @clear="handleFilterClear"
        >
          <el-button slot="append" @click="fetchOrders" icon="el-icon-search"></el-button>
        </el-input>
      </div>
  
      <div v-if="ordersList.length === 0" class="empty-container">
        <el-empty description="未找到符合条件的订单数据"></el-empty>
      </div>
  
      <div v-else class="orders-container">
        <div class="item-order" v-for="(orderInfo, index) in ordersList" :key="index">
          <div class="orders-base-info">
            <div class="code">订单号：{{ orderInfo.code }}</div>
            <div class="code" v-if="orderInfo.tradeStatus">支付时间：{{ orderInfo.tradeTime }}</div>
            <div class="code" v-if="orderInfo.isRefundConfirm">退款时间：{{ orderInfo.refundTime }}</div>
          </div>
          <div class="info">
            <div class="img-wrap">
              <img :src="orderInfo.cover" alt="" />
            </div>
            <div class="info-content">
              <div class="title-row">
                <span class="title">{{ orderInfo.productTitle }}</span>
                <span class="number">x{{ orderInfo.buyNumber }}</span>
              </div>
              <div class="detail">备注：{{ orderInfo.detail }}</div>
              <div class="price-row">
                <span class="symbol">￥</span>
                <span class="price">{{ totalPrice(orderInfo) }}</span>
              </div>
              <div class="orders-base-info">创建时间：{{ orderInfo.createTime }}</div>
              <div class="action-row">
                <span
                  v-if="orderInfo.refundStatus"
                  class="refund-status"
                  :class="{ confirmed: orderInfo.isRefundConfirm }"
                >
                  {{ orderInfo.isRefundConfirm ? '钱款已原路返回' : '申请退款中，待卖家确认...' }}
                </span>
                <template v-else>
                  <el-button
                    type="primary"
                    size="small"
                    @click="placeAnOrder(orderInfo)"
                    v-if="!orderInfo.tradeStatus"
                  >
                    下单
                  </el-button>
                  <el-button
                    type="warning"
                    size="small"
                    @click="refund(orderInfo)"
                    v-else
                  >
                    申请退款
                  </el-button>
                  <el-button
                    type="danger"
                    size="small"
                    style="margin-left: 8px;"
                    @click="del(orderInfo)"
                    v-if="orderInfo.tradeStatus"
                  >
                    删除
                  </el-button>
                </template>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>
<script>
export default {
    name: 'BuyOrders',
    data() {
        return {
            ordersList: [],
            ordersQueryDto: {},
            tradeStatusSelectedItem: {},
            tradeStatusList: [{ tradeStatus: null, name: '全部' }, { tradeStatus: true, name: '已支付' }, { tradeStatus: false, name: '未支付' }]
        }
    },
    created() {
        this.fetchOrders();
        this.tradeStatusSelected(this.tradeStatusList[0]);
    },
    methods: {
        tradeStatusSelected(tradeStatusItem) {
            this.tradeStatusSelectedItem = tradeStatusItem;
            this.ordersQueryDto.tradeStatus = tradeStatusItem.tradeStatus;
            this.fetchOrders();
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
.condition {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;

  .trade {
    font-size: 13px;
    background-color: #f0f6ff;
    border-radius: 6px;
    padding: 6px 12px;

    span {
      display: inline-block;
      padding: 6px 14px;
      border-radius: 4px;
      color: #409eff;
      cursor: pointer;
      transition: background-color 0.3s ease;

      &.active,
      &:hover {
        background-color: #409eff;
        color: #fff;
      }
    }
  }
}

.empty-container {
  margin-top: 60px;
}

.orders-container {
  display: flex;
  flex-direction: column;
  gap: 20px;

  .item-order {
    background-color: #fff;
    border-radius: 12px;
    box-shadow: 0 2px 10px rgba(64, 158, 255, 0.15);
    padding: 20px;

    .orders-base-info {
      font-size: 13px;
      color: #6a779c;
      margin-bottom: 14px;

      .code {
        margin: 4px 0;
      }
    }

    .info {
      display: flex;
      gap: 20px;
      align-items: flex-start;

      .img-wrap {
        flex-shrink: 0;
        width: 140px;
        height: 140px;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);

        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
          display: block;
        }
      }

      .info-content {
        flex-grow: 1;

        .title-row {
          display: flex;
          align-items: center;
          gap: 10px;
          font-size: 18px;
          font-weight: 700;
          color: #273a7a;
          margin-bottom: 8px;

          .number {
            font-weight: 500;
            color: #666;
          }
        }

        .detail {
          font-size: 14px;
          color: #4a5ab4;
          margin-bottom: 12px;
        }

        .price-row {
          display: flex;
          align-items: baseline;
          gap: 4px;
          margin-bottom: 12px;

          .symbol {
            font-size: 14px;
            color: #409eff;
            font-weight: 700;
          }

          .price {
            font-size: 22px;
            font-weight: 800;
            color: #1a3a8f;
          }
        }

        .orders-base-info {
          font-size: 13px;
          color: #8294c4;
          margin-bottom: 12px;
        }

        .action-row {
          display: flex;
          align-items: center;
          gap: 10px;

          .refund-status {
            color: #1a73e8;
            font-weight: 600;

            &.confirmed {
              color: #3a885a;
            }
          }

          .el-button {
            padding: 4px 14px;
            font-size: 13px;
          }
        }
      }
    }
  }
}
</style>