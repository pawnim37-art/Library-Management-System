import API from './api';

export const notificationService = {
  getMyNotifications: async () => {
    const response = await API.get('/notifications');
    return response.data;
  },

  getUnreadCount: async () => {
    const response = await API.get('/notifications/unread-count');
    return response.data.unreadCount;
  },

  markAsRead: async (id) => {
    const response = await API.put(`/notifications/${id}/read`);
    return response.data;
  },

  markAllAsRead: async () => {
    const response = await API.put('/notifications/read-all');
    return response.data;
  }
};
