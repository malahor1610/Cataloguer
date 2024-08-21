import { useEffect, useState } from 'react';
import { DragDropContext, Draggable, Droppable } from 'react-beautiful-dnd';
import '../App.css';
import Page from '../shell/Page';
import Tile from '../show/Tile';

export default function Watchlist() {
  const [shows, setShows] = useState([]);

  useEffect(() => {
    const searchParams = new URLSearchParams({
      sort: "position"
    }).toString();
    fetch("/api/shows?" + searchParams)
      .then(result => result.json())
      .then(result => setShows(result._embedded.shows));
  }, []);

  function handleDragEnd(destination, source) {
    if (!destination) return;
    let list = reorder(shows, source.index, destination.index);
    list.forEach(element => {
      element.position = list.indexOf(element) + 1;
    });
    updatePositions(list);
  };

  function reorder(list, startIndex, endIndex) {
    const result = Array.from(list);
    const [removed] = result.splice(startIndex, 1);
    result.splice(endIndex, 0, removed);
    return result;
  };

  function updatePositions(list) {
    setShows(list);
    fetch('/api/shows/positions', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(list)
    })
      .then(response => response.json())
      .then(data => setShows(data))
      .catch(error => console.error('Error updating show:', error));
  }

  return (
    <Page content={(<>
      <DragDropContext onDragEnd={({ destination, source }) => handleDragEnd(destination, source)}>
        <Droppable droppableId="droppable">
          {(provided) => (
            <div className='w-100' ref={provided.innerRef} {...provided.droppableProps}>
              {shows.map((show, index) =>
                <Draggable
                  key={show.position}
                  index={index}
                  draggableId={'drag-' + show.position}
                >
                  {(provided, snapshot) => (
                    <div
                      ref={provided.innerRef}
                      {...provided.draggableProps}
                      {...provided.dragHandleProps}
                    >
                      <Tile show={show} orderable />
                    </div>
                  )}
                </Draggable>
              )}
            </div>
          )}
        </Droppable>
      </DragDropContext>
    </>)} />
  );
}
