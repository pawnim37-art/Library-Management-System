import API from './api';

export const fineService = {
  getMyFines: async () => {
    const response = await API.get('/fines/my-fines');
    return response.data;
  },

  getAllFines: async () => {
    const response = await API.get('/fines');
    return response.data;
  },

  payFine: async (id, paymentData = { paymentMethod: 'CREDIT_CARD' }) => {
    const response = await API.post(`/fines/${id}/pay`, paymentData);
    return response.data;
  },

  waiveFine: async (id, reason = 'Administrative waiver') => {
    const response = await API.post(`/fines/${id}/waive`, { reason });
    return response.data;
  }
};
