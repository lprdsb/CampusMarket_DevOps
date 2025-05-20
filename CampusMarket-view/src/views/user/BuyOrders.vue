<template>
    <div>
        <div class="item-order" v-for="(orderInfo, index) in ordersList" :key="index">
            <div class="code">订单号：{{ orderInfo.code }}</div>
            <div class="info">
                <div>
                    <img :src="orderInfo.cover" alt="" srcset="">
                </div>
                <div>
                    <div>
                        <span class="title">{{ orderInfo.productTitle }}</span>
                        <span class="number">x{{ orderInfo.buyNumber }}</span>
                    </div>
                    <div class="detail">
                        备注：{{ orderInfo.detail }}
                    </div>
                    <div>
                        <span class="symbol">￥</span>
                        <span class="price">{{ totalPrice(orderInfo) }}</span>
                    </div>
                    <div style="margin-block: 10px;">
                        <span v-if="orderInfo.refundStatus" style="color: rgb(0, 121, 186);">
                            {{ orderInfo.isRefundConfirm ? '钱款已原路返回' : '申请退款中，待卖家确认...' }}
                        </span>
                        <span v-else>
                            <span @click="placeAnOrder(orderInfo)" v-if="!orderInfo.tradeStatus"
                                class="edit-button">下单</span>
                            <span @click="refund(orderInfo)" v-else class="edit-button">申请退款</span>
                        </span>

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
        }
    },
    created() {
        this.fetchOrders();
    },
    methods: {
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
.item-order {
    font-size: 14px;
    margin-block: 10px;
    padding-bottom: 10px;
    border-bottom: 1px solid rgb(246, 246, 246);

    .code {
        margin-block: 10px;
    }

    .info {
        .detail {
            margin-block: 4px;
            font-size: 14px;
        }

        img {
            width: 140px;
        }

        display: flex;
        justify-content: left;
        gap: 20px;

        .symbol {
            font-size: 12px;
            margin-right: 4px;
            color: rgb(255, 79, 36);
        }

        .price {
            font-size: 18px;
            font-weight: 800;
            color: rgb(255, 79, 36);
        }

        .title {
            font-size: 20px;
            font-weight: 800;
        }

        .number {
            margin-left: 6px;
        }
    }
}
</style>