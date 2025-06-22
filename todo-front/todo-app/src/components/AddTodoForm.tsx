import { useState } from "react";
import { fetchWithAuth } from "../utils/fetchWithAuth";
import type { Todo } from "../types/types";

interface AddTodoFormProps {
  onAdd: (todo: Todo) => void;
}

const AddTodoForm: React.FC<AddTodoFormProps> = ({ onAdd }) => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [priority, setPriority] = useState<"LOW" | "MEDIUM" | "HIGH">("LOW");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const res = await fetchWithAuth(`/todos/add-todo?priority=${priority}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ title, description }),
    });

    if (res.ok) {
      const newTodo: Todo = await res.json();
      onAdd(newTodo);
      setTitle("");
      setDescription("");
      setPriority("LOW");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="todo-form">
      <input
        required
        placeholder="Title"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
      />
      <input
        placeholder="Description"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
      />
      <select
        value={priority}
        onChange={(e) =>
          setPriority(e.target.value as "LOW" | "MEDIUM" | "HIGH")
        }
      >
        <option value="LOW">Low</option>
        <option value="MEDIUM">Medium</option>
        <option value="HIGH">High</option>
      </select>
      <button type="submit">Save 📝</button>
    </form>
  );
};

export default AddTodoForm;
