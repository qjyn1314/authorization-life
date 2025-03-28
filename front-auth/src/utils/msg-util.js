import {h} from 'vue';
import {ElMessage, ElNotification} from "element-plus";

class Message {
    info(message = '提示信息') {
        return ElMessage({
            message: message,
            type: 'info',
            plain: true,
            showClose: true,
            center: true,
            grouping: true,
        });
    }

    success(message = '成功信息') {
        return ElMessage({
            message: message,
            type: 'success',
            plain: true,
            showClose: true,
            center: true,
            grouping: true,
        });
    }

    warning(message = '警告信息') {
        return ElMessage({
            message: message,
            type: 'warning',
            plain: true,
            showClose: true,
            center: true,
            grouping: true,
        });
    }

    error(message = '错误信息') {
        return ElMessage({
            message: message,
            type: 'error',
            plain: true,
            showClose: true,
            center: true,
            grouping: true,
        });
    }

    noticeInfo(msg = '提示信息') {
        return ElNotification({
            title: 'Info',
            message: h('i', {style: 'color: teal'}, msg),
            type: 'info',
            duration: 900,
            showClose: false
        })
    }

}

export const prompt = new Message();
