import { Col, Container } from 'reactstrap';
import '../App.css';
import Tile from '../show/Tile';

export default function SearchResults({ shows, setError }) {

  return (
    <Container className='row text-center'>
      {shows.map(show =>
        <Col xs='2' className='my-2' key={show.id}>
          <Tile show={show} search setError={setError}/>
        </Col>
      )}
    </Container>
  );
}
