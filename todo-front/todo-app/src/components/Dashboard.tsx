import { useEffect, useState } from "react";
import { fetchWithAuth } from "../utils/fetchWithAuth";
import { useNavigate } from "react-router-dom";
import type { Todo } from "../types/types";
import AddTodoForm from "./AddTodoForm";
import EditTodoForm from "./EditTodoForm";

const Dashboard = () => {
  const [todos, setTodos] = useState<Todo[]>([]);
  const navigate = useNavigate();
  const [editingTodo, setEditingTodo] = useState<Todo | null>(null);

  useEffect(() => {
    const getTodos = async () => {
      const res = await fetchWithAuth("/todos/my-todos");

      if (!res.ok) {
        if (res.status === 403) {
          navigate("/login");
        }
        return;
      }

      const data: Todo[] = await res.json();
      setTodos(data);
    };

    getTodos();
  }, [navigate]);

  const handleSaveEdit = (updated: Todo) => {
    setTodos((prev) =>
      prev.map((t) => (t.id === updated.id ? updated : t))
    );
    setEditingTodo(null);
  };
  
  const handleToggle = async (id: number) => {
    const res = await fetchWithAuth(`/todos/${id}/status`, { method: "PUT" });
    if (res.ok) {
      const updated: Todo = await res.json();
      setTodos((prev) =>
        prev.map((t) => (t.id === updated.id ? updated : t))
      );
    }
  };

  const handleDelete = async (id: number) => {
    const res = await fetchWithAuth(`/todos/delete-todo/${id}`, {
      method: "DELETE",
    });
    if (res.ok) {
      setTodos((prev) => prev.filter((t) => t.id !== id));
    }
  };

  return (
    <div className="dashboard-container">
      <div className="todo-app-title">
        <h1>TodoApp</h1>
        <button
          onClick={() => {
            localStorage.removeItem("token");
            navigate("/");
          }}
        >
          Logout
        </button>
      </div>
  
      {editingTodo ? (
        <EditTodoForm
          todo={editingTodo}
          onCancel={() => setEditingTodo(null)}
          onSave={handleSaveEdit}
        />
      ) : (
        <>
          <AddTodoForm
            onAdd={(newTodo: Todo) => setTodos((prev) => [...prev, newTodo])}
          />
  
          <h2>Your todos:</h2>
          <ul>
            {todos.map((todo) => (
              <li key={todo.id}>
                <span
                  onClick={() => handleToggle(todo.id)}
                  style={{ cursor: "pointer", marginRight: 10 }}
                >
                  {todo.completed ? "✅" : "❌"} {todo.title} {todo.description}{" "}
                  {todo.priority}
                </span>
                <button onClick={() => setEditingTodo(todo)}>✏️</button>
                <button onClick={() => handleDelete(todo.id)}>🗑️</button>
              </li>
            ))}
          </ul>
        </>
      )}
    </div>
  );
};

export default Dashboard;
