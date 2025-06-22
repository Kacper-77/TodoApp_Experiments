export interface Todo {
    id: number;
    title: string;
    description: string;
    completed: boolean;
    priority: "LOW" | "MEDIUM" | "HIGH";
  }

enum role {
  USER,
  ADMIN
}

export interface User {
  id: number;
  username: string;
  email: string;
  password: string;
  age: number;
  phone_number: string
  role: role;
}