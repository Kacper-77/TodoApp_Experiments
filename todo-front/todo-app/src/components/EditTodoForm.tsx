import { useState } from "react";
import type { Todo } from "../types/types";
import { fetchWithAuth } from "../utils/fetchWithAuth";

type Props = {
  todo: Todo;
  onCancel: () => void;
  onSave: (updated: Todo) => void;
};

const EditTodoForm = ({ todo, onCancel, onSave }: Props) => {
  const [title, setTitle] = useState(todo.title);
  const [description, setDescription] = useState(todo.description);
  const [priority, setPriority] = useState<Todo["priority"]>(todo.priority);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const res = await fetchWithAuth(
      `/todos/update-todo/${todo.id}?priority=${priority}`,
      {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title, description }),
      }
    );

    if (res.ok) {
      const updated = await res.json();
      onSave(updated);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="todo-form">
      <input
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        placeholder="Title"
        required
      />
      <input
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        placeholder="Description"
      />
      <select value={priority} onChange={(e) => setPriority(e.target.value as Todo["priority"])}>
        <option value="LOW">LOW</option>
        <option value="MEDIUM">MEDIUM</option>
        <option value="HIGH">HIGH</option>
      </select>
      <button type="submit">💾 Save</button>
      <button type="button" onClick={onCancel}>
        ❌ Cancel
      </button>
    </form>
  );
};

export default EditTodoForm;
