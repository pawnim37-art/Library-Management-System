import API from './api';

export const circulationService = {
  issueBook: async (issueData) => {
    const response = await API.post('/circulation/issue', issueData);
    return response.data;
  },

  borrowBook: async (bookId) => {
    const response = await API.post(`/circulation/borrow/${bookId}`);
    return response.data;
  },

  returnBook: async (returnData) => {
    const response = await API.post('/circulation/return', returnData);
    return response.data;
  },

  renewBook: async (transactionId) => {
    const response = await API.post(`/circulation/renew/${transactionId}`);
    return response.data;
  },

  reserveBook: async (bookId) => {
    const response = await API.post(`/circulation/reserve/${bookId}`);
    return response.data;
  },

  cancelReservation: async (reservationId) => {
    const response = await API.delete(`/circulation/reserve/${reservationId}`);
    return response.data;
  },

  getMyLoans: async () => {
    const response = await API.get('/circulation/my-loans');
    return response.data;
  },

  getMyReservations: async () => {
    const response = await API.get('/circulation/my-reservations');
    return response.data;
  },

  getAllLoans: async () => {
    const response = await API.get('/circulation/all-loans');
    return response.data;
  },

  getAllReservations: async () => {
    const response = await API.get('/circulation/all-reservations');
    return response.data;
  }
};
