import API from './api';

export const bookService = {
  getAllBooks: async (query = '', categoryId = '', onlyAvailable = false) => {
    const params = new URLSearchParams();
    if (query) params.append('query', query);
    if (categoryId) params.append('categoryId', categoryId);
    if (onlyAvailable) params.append('onlyAvailable', 'true');

    const response = await API.get(`/books?${params.toString()}`);
    return response.data;
  },

  getBookById: async (id) => {
    const response = await API.get(`/books/${id}`);
    return response.data;
  },

  createBook: async (bookData) => {
    const response = await API.post('/books', bookData);
    return response.data;
  },

  updateBook: async (id, bookData) => {
    const response = await API.put(`/books/${id}`, bookData);
    return response.data;
  },

  deleteBook: async (id) => {
    const response = await API.delete(`/books/${id}`);
    return response.data;
  },

  uploadCover: async (file) => {
    const formData = new FormData();
    formData.append('file', file);
    const response = await API.post('/books/upload-cover', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data.imageUrl;
  },

  importCSV: async (file) => {
    const formData = new FormData();
    formData.append('file', file);
    const response = await API.post('/books/import-csv', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
  }
};
