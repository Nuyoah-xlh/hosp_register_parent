import request from '@/utils/request'

const api_name = '/admin/cmn/dict'

export default {
    //根据dictCode查询下级节点
    findByDictCode(dictCode) {
        return request({
            url: `${api_name}/findByDictCode/${dictCode}`,
            method: 'get'
        })
    },

    findByParentId(parentId) {
        return request({
            url: `${api_name}/findChildData/${parentId}`,
            method: 'get'
        })
    }
}