package org.marco.calamai.todolist.services;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.marco.calamai.todolist.exceptions.InvalidTimeException;
import org.marco.calamai.todolist.exceptions.ToDoNotFoundException;
import org.marco.calamai.todolist.exceptions.WrongUsernameException;
import org.marco.calamai.todolist.model.ToDo;
import org.marco.calamai.todolist.repositories.mongo.ToDoMongoRepository;


@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for ToDoService")
class ToDoServiceTest {
	
	@Mock
	private ToDoMongoRepository toDoMongoRepository;
	
	@InjectMocks
	private ToDoService toDoService;
	
	@Nested
	@DisplayName("Tests for insert ToDo")
	class InsertToDo{
		
		@Test @DisplayName("Insert happy case")
		void testInsertToDo() {
			LocalDate deadline = LocalDate.now();
			String user1 = "username_1"; 
			ToDo toSave = spy(new ToDo(user1, "to_save", "to_save_description", deadline));
			ToDo saved = new ToDo(user1, "saved", "saved_description", deadline.plusDays(1));
			when(toDoMongoRepository.save(any(ToDo.class))).thenReturn(saved);
			ToDo result = toDoService.insertToDo(toSave);
			assertThat(result).isSameAs(saved); 
			InOrder inOrder = inOrder(toSave, toDoMongoRepository);
			inOrder.verify(toSave).setId(null);
			inOrder.verify(toDoMongoRepository).save(toSave);
		}
	
		@Test @DisplayName("Insert ToDo when date is before today")
		void testInsertToDoWithDateBeforeTodayShouldThrow() {
			LocalDate deadline = LocalDate.now().minusDays(1);
			String user1 = "username_1"; 
			ToDo toSave = new ToDo(user1, "to_save", "to_save_description", deadline);
			assertThatThrownBy(() -> toDoService.insertToDo(toSave))
			.isInstanceOf(InvalidTimeException.class)
			.hasMessage(ToDoService.DATE_IS_BEFORE_TODAY);
		}	
	}	
	
	@Nested
	@DisplayName("Test for update ToDo")
	class UpdateToDo{
		
		@Test @DisplayName("Update happy case")
		void testUpdateToDo() {
			LocalDate deadline = LocalDate.now();
			String user1 = "username_1"; 
			BigInteger id = new BigInteger("0");
			ToDo toUpdate = spy(new ToDo(user1, "to_update", "to_update_description", deadline));
			ToDo updated = new ToDo(user1, "saved", "saved_description", deadline.plusDays(1));
			updated.setId(id);
			when(toDoMongoRepository.save(any(ToDo.class))).thenReturn(updated);
			ToDo result = toDoService.updateToDo(id, user1,  toUpdate);
			assertThat(result).isSameAs(updated);
			InOrder inOrder = inOrder(toUpdate, toDoMongoRepository);
			inOrder.verify(toUpdate).setId(id);
			inOrder.verify(toDoMongoRepository).save(toUpdate);
		}
		
		@Test @DisplayName("Update ToDo when date is before today")
		void testUpdateToDoWithDateBeforeTodayShouldThrow() {
			LocalDate deadline = LocalDate.now().minusDays(1);
			String user1 = "username_1"; 
			ToDo toSave = new ToDo(user1, "to_save", "to_save_description", deadline);
			BigInteger id = new BigInteger("0");
			assertThatThrownBy(() -> toDoService.updateToDo(id, user1, toSave))
			.isInstanceOf(InvalidTimeException.class)
			.hasMessage(ToDoService.DATE_IS_BEFORE_TODAY);
		}
		
		@Test @DisplayName("Update ToDo when username is wrong")
		void testUpdateToDoWhenUsernameIsWrongShouldThrow() {
			LocalDate deadline = LocalDate.now();
			String user1 = "username_1"; 
			ToDo toSave = new ToDo(user1, "to_save", "to_save_description", deadline);
			BigInteger id = new BigInteger("0");
			assertThatThrownBy(() -> toDoService.updateToDo(id, "username_2", toSave))
			.isInstanceOf(WrongUsernameException.class)
			.hasMessage(ToDoService.WRONG_USERNAME);
		}
	}
	
	@Nested
	@DisplayName("Test for find ToDo")
	class FindToDo{
		
		@Test  @DisplayName("Find all ToDo sorted by done and deadline")
		void testFindAllToDoOrderByDoneAscDeadlineAsc(){
			ToDo toDo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now().plusMonths(1));
			ToDo toDo2 = new ToDo("username_2", "title_2", "description_2", LocalDate.now().plusDays(1));
			ToDo toDo3 = new ToDo("username_3", "title_3", "description_3", LocalDate.now());
			toDo2.setDone(true);
			when(toDoMongoRepository.findByOrderByDoneAscDeadlineAsc()).thenReturn(asList(toDo3, toDo1, toDo2));
			List<ToDo> result = toDoService.findAllToDoOrderByDoneAscDeadlineAsc();
			assertThat(result).containsExactly(toDo3, toDo1, toDo2);
			verify(toDoMongoRepository, times(1)).findByOrderByDoneAscDeadlineAsc();
		}
		
		@Test @DisplayName("Find all ToDo by user name sorted by done and deadline")
		void testFindToDoByUserOrderByDoneAscDeadlineAsc() {
			String user1 = "a_username"; 
			String userToFind = "usernameToFind";
			ToDo toDo1 = new ToDo(user1, "title_1", "description_1", LocalDate.now());
			ToDo toDo2 = new ToDo(userToFind, "title_2", "description_2", LocalDate.now().plusMonths(1));
			ToDo toDo3 = new ToDo(userToFind, "title_3", "description_3", LocalDate.now().plusDays(1));
			ToDo toDo4 = new ToDo(userToFind, "title_4", "description_4", LocalDate.now());
			toDo3.setDone(true);
			when(toDoMongoRepository.findByOrderByDoneAscDeadlineAsc()).thenReturn(asList(toDo1, toDo4, toDo2, toDo3));
			List<ToDo> result = toDoService.findToDoByUserOrderByDoneAscDeadlineAsc(userToFind);
			assertThat(result).containsExactly(toDo4, toDo2, toDo3);
			verify(toDoMongoRepository, times(1)).findByOrderByDoneAscDeadlineAsc();
		}
		
		
		@Test @DisplayName("Find all ToDo by user name sorted sorted by done and deadline where there are not")
		void testFindToDoByUserOrderByDoneAscDeadlineAscWhereThereAreNot() {
			String user1 = "a_username"; 
			String userToFind = "usernameToFind";
			ToDo toDo1 = new ToDo(user1, "title_1", "description_1", LocalDate.now());
			when(toDoMongoRepository.findByOrderByDoneAscDeadlineAsc()).thenReturn(asList(toDo1));
			List<ToDo> result = toDoService.findToDoByUserOrderByDoneAscDeadlineAsc(userToFind);
			assertEquals(0, result.size());
			verify(toDoMongoRepository, times(1)).findByOrderByDoneAscDeadlineAsc();
		}
	
		@Test @DisplayName("Find ToDo by id")
		void testFindToDoById(){
			ToDo toDo1 = new ToDo("username1", "title_1", "description_1", LocalDate.now());
			BigInteger id = new BigInteger("0");
			toDo1.setId(new BigInteger("0"));
			when(toDoMongoRepository.findById(id)).thenReturn(Optional.of(toDo1));
			ToDo result = toDoService.findToDoById(id);
			assertThat(result).isSameAs(toDo1); 
			verify(toDoMongoRepository, times(1)).findById(id);
		}
		
		@Test @DisplayName("Find ToDo by id when it is not found")
		void testFindToDoByIdWhenNotFoundShouldThrow() {
			BigInteger id = new BigInteger("0");
			when(toDoMongoRepository.findById(id)).thenReturn(Optional.empty());
			assertThatThrownBy(() -> toDoService.findToDoById(id))
			.isInstanceOf(ToDoNotFoundException.class)
			.hasMessage(ToDoService.TO_DO_NOT_FOUND);
			verify(toDoMongoRepository, times(1)).findById(id);
		}
	}
	@Nested
	@DisplayName("Test for delete ToDo")
	class DeleteToDo{
		@Test @DisplayName("Delete ToDo by id")
		void testDeleteToDoById() {
			String user1 = "username_1"; 
			ToDo toDo1 = new ToDo(user1, "title_1", "description_1", LocalDate.now());
			BigInteger id = new BigInteger("0");
			toDo1.setId(new BigInteger("0"));
			when(toDoMongoRepository.findById(id)).thenReturn(Optional.of(toDo1));
			ToDo result = toDoService.deleteToDoById(id, user1);
			assertThat(result).isSameAs(toDo1);
			InOrder inOrder = inOrder(toDoMongoRepository);
			inOrder.verify(toDoMongoRepository).findById(id);
			inOrder.verify(toDoMongoRepository).deleteById(id);			
		}
		
		@Test @DisplayName("Delete ToDo by id when it is not found")
		void testDeleteToDoByIdWhenNotFoundShouldThrow() {
			String user1 = "username_1"; 
			BigInteger id = new BigInteger("0");
			when(toDoMongoRepository.findById(id)).thenReturn(Optional.empty());
			assertThatThrownBy(() -> toDoService.deleteToDoById(id, user1))
			.isInstanceOf(ToDoNotFoundException.class)
			.hasMessage(ToDoService.TO_DO_NOT_FOUND);	
			verify(toDoMongoRepository).findById(id);	
		}
		
		
		@Test @DisplayName("Delete ToDo by id when username is wrong")
		void testDeleteToDoByIdWhenUsernameIsWrongShouldThrow() {
			String userToFind = "usernameToFind"; 
			ToDo toDo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now());
			BigInteger id = new BigInteger("0");
			toDo1.setId(new BigInteger("0"));
			when(toDoMongoRepository.findById(id)).thenReturn(Optional.of(toDo1));
			assertThatThrownBy(() -> toDoService.deleteToDoById(id, userToFind))
			.isInstanceOf(WrongUsernameException.class)
			.hasMessage(ToDoService.WRONG_USERNAME);
			verify(toDoMongoRepository, times(1)).findById(id);
			verifyNoMoreInteractions(toDoMongoRepository);
		}
	}
	
	
}

	
