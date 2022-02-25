import request from '@/utils/request'

//多条件分页查询
export default {
    // 获取医院设置列表
    getDistList(id) {
        return request({
            url: `/admin/cmn/dict/findChildData/${id}`,
            method: 'get'
        })
    }

}